package com.ruoyi.utils;

import com.alibaba.fastjson2.JSON;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FastGptUtils {

    private static final String DEFAULT_BASE_URL = "https://cloud.fastgpt.cn/api";
    private static final int CONNECT_TIMEOUT = 10_000;
    private static final int SOCKET_TIMEOUT = 90_000;
    private static final int REQ_TIMEOUT = 10_000;

    private FastGptUtils() {
    }

    public static String chat(String chatId, String question) {
        String apiKey = readConfig("FASTGPT_API_KEY", null);
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("FASTGPT_API_KEY is not configured");
        }

        String baseUrl = readConfig("FASTGPT_BASE_URL", DEFAULT_BASE_URL);
        String url = trimRightSlash(baseUrl) + "/v1/chat/completions";

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("chatId", chatId == null ? "" : chatId);
        body.put("stream", false);
        body.put("detail", false);

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(message("user", question == null ? "" : question));
        body.put("messages", messages);

        String response = postJson(url, JSON.toJSONString(body), apiKey);
        return extractAnswer(response);
    }

    private static String postJson(String url, String jsonBody, String apiKey) {
        HttpPost post;
        try {
            post = new HttpPost(new URIBuilder(url).build());
        } catch (Exception e) {
            throw new RuntimeException("Invalid FastGPT URL: " + url, e);
        }

        post.setHeader("Authorization", "Bearer " + apiKey);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-Type", "application/json; charset=UTF-8");
        post.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));

        try (CloseableHttpClient client = buildClient();
             CloseableHttpResponse resp = client.execute(post)) {
            int code = resp.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8);
            if (code / 100 != 2) {
                throw new RuntimeException("FastGPT HTTP " + code + ": " + body);
            }
            return body;
        } catch (Exception e) {
            throw new RuntimeException("FastGPT request failed: " + e.getMessage(), e);
        }
    }

    private static CloseableHttpClient buildClient() {
        RequestConfig rc = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectionRequestTimeout(REQ_TIMEOUT)
                .setExpectContinueEnabled(false)
                .build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        cm.setDefaultMaxPerRoute(20);

        HttpClientBuilder builder = HttpClients.custom()
                .setDefaultRequestConfig(rc)
                .setConnectionManager(cm)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
                .setUserAgent("ruoyi-ai/1.0 (+fastgpt)");
        return builder.build();
    }

    private static String extractAnswer(String response) {
        try {
            Map<?, ?> map = JSON.parseObject(response, Map.class);
            List<?> choices = (List<?>) map.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<?, ?> first = (Map<?, ?>) choices.get(0);
                Map<?, ?> msg = (Map<?, ?>) first.get("message");
                Object content = msg == null ? null : msg.get("content");
                return content == null ? "" : content.toString();
            }
            return response;
        } catch (Exception e) {
            return response;
        }
    }

    private static Map<String, String> message(String role, String content) {
        Map<String, String> msg = new LinkedHashMap<>();
        msg.put("role", role);
        msg.put("content", content);
        return msg;
    }

    private static String readConfig(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            value = System.getProperty(key);
        }
        return value == null || value.isBlank() ? defaultValue : value.trim();
    }

    private static String trimRightSlash(String value) {
        while (value.endsWith("/")) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }
}
