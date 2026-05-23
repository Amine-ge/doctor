package com.ruoyi.utils;

import com.alibaba.fastjson2.JSON;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class DashScopeUtils {

    // 兼容模式（OpenAI式）Chat Completions
    private static final String API_URL =
            "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";

    // 默认模型，可按需覆盖
    private static final String DEFAULT_MODEL = "qwen-plus";

    // 超时配置（毫秒）
    private static final int CONNECT_TIMEOUT = 15_000;
    private static final int SOCKET_TIMEOUT  = 60_000;
    private static final int REQ_TIMEOUT     = 15_000;

    /** 一次性客户端（让 HttpClient 自己管理 SSL 与连接池，不再手工注册 https 工厂） */
    private static CloseableHttpClient buildClient() {
        RequestConfig rc = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectionRequestTimeout(REQ_TIMEOUT)
                .setExpectContinueEnabled(false) // 避免某些网关对 100-continue 处理不佳
                .build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        cm.setDefaultMaxPerRoute(20);

        HttpClientBuilder builder = HttpClients.custom()
                .setDefaultRequestConfig(rc)
                .setConnectionManager(cm)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
                .disableContentCompression() // 避免中间盒对压缩处理的兼容性问题
                .setUserAgent("ruoyi-ai/1.0 (+dashscope)");

        // 可选：读取 JVM/system 代理（如必须走代理）
        HttpHost proxy = readProxyFromSystemProps();
        if (proxy != null) {
            builder.setRoutePlanner(new DefaultProxyRoutePlanner(proxy));
        }
        return builder.build();
    }

    private static HttpHost readProxyFromSystemProps() {
        // 优先 https 代理
        String host = System.getProperty("https.proxyHost", System.getProperty("http.proxyHost"));
        String port = System.getProperty("https.proxyPort", System.getProperty("http.proxyPort"));
        if (host != null && !host.isBlank() && port != null && !port.isBlank()) {
            try { return new HttpHost(host.trim(), Integer.parseInt(port.trim()), "http"); }
            catch (Exception ignored) {}
        }
        return null;
    }

    /** 对外：最常用方法，传模型 + system + question */
    public static String chat(String model, String systemPrompt, String question) {
        String apiKey = apiKey();
        if (apiKey == null) {
            throw new IllegalStateException("DASHSCOPE_API_KEY 未配置（请用环境变量或 -DDASHSCOPE_API_KEY=xxx）");
        }
        if (model == null || model.isBlank()) model = DEFAULT_MODEL;

        // OpenAI 兼容消息体
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", model);
        List<Map<String, String>> messages = new ArrayList<>();
        if (systemPrompt != null && !systemPrompt.isBlank()) {
            messages.add(msg("system", systemPrompt));
        }
        messages.add(msg("user", question == null ? "" : question));
        body.put("messages", messages);

        String json = JSON.toJSONString(body);
        String resp = postJson(API_URL, json, apiKey);

        // 提取 content
        try {
            Map<?, ?> map = JSON.parseObject(resp, Map.class);
            List<?> choices = (List<?>) map.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<?, ?> c0 = (Map<?, ?>) choices.get(0);
                Map<?, ?> msg = (Map<?, ?>) c0.get("message");
                Object content = msg == null ? null : msg.get("content");
                return content == null ? "" : content.toString();
            }
            return resp; // 兜底：返回原始 JSON 便于排查
        } catch (Exception e) {
            // 返回原始响应帮助定位
            return resp;
        }
    }

    /** 极简问答（固定 system） */
    public static String ask(String question) {
        return chat(DEFAULT_MODEL, "You are a helpful assistant.", question);
    }

    /** 低层 POST JSON（默认 SSL/连接池，无自定义 https 工厂） */
    private static String postJson(String url, String jsonBody, String apiKey) {
        HttpPost post;
        try {
            post = new HttpPost(new URIBuilder(url).build());
        } catch (Exception e) {
            throw new RuntimeException("无效的 URL: " + url, e);
        }
        post.setHeader("Authorization", "Bearer " + apiKey);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-Type", "application/json; charset=UTF-8");
        post.setHeader("Connection", "close"); // 避免少数网关 keep-alive reset
        post.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));

        try (CloseableHttpClient client = buildClient();
             CloseableHttpResponse resp = client.execute(post)) {
            int code = resp.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8);
            if (code / 100 != 2) {
                throw new RuntimeException("DashScope HTTP " + code + ": " + body);
            }
            return body;
        } catch (java.net.SocketException se) {
            throw new RuntimeException("网络异常(Connection reset/代理/防火墙)：" + se.getMessage(), se);
        } catch (Exception e) {
            throw new RuntimeException("DashScope 请求异常: " + e.getMessage(), e);
        }
    }

    private static Map<String, String> msg(String role, String content) {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("role", role);
        m.put("content", content);
        return m;
    }

    /** 从 环境变量 / JVM 参数 读取 Key（不要硬编码） */
    private static String apiKey() {
        String k = System.getenv("DASHSCOPE_API_KEY");
        if (k == null || k.isBlank()) k = System.getProperty("DASHSCOPE_API_KEY");
        return (k == null || k.isBlank()) ? null : k.trim();
    }

    // 便捷 main：本地跑一下看网络/代理是否正常
    public static void main(String[] args) {
        System.setProperty("DASHSCOPE_API_KEY", "替换成你的key"); // 或用环境变量
        // 如需代理：
//        System.setProperty("https.proxyHost", "127.0.0.1");
//        System.setProperty("https.proxyPort", "7890");

        String out = chat("qwen-plus",
                "You are a helpful assistant.",
                "你好！用一句话介绍一下你自己。");
        System.out.println(out);
    }
}
