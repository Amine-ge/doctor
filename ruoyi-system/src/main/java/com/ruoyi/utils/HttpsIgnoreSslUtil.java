package com.ruoyi.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;

public class HttpsIgnoreSslUtil {

    private HttpsIgnoreSslUtil() {
    }

    public static String doGet(String url) {
        try (CloseableHttpClient httpClient = createIgnoreSslHttpClient();
             CloseableHttpResponse response = httpClient.execute(new HttpGet(url))) {
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("HTTPS GET request failed: " + url, e);
        }
    }

    private static CloseableHttpClient createIgnoreSslHttpClient() throws Exception {
        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (TrustStrategy) (X509Certificate[] chain, String authType) -> true)
                .build();

        return HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
    }
}
