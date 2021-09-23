package com.cn.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.List;

@Slf4j
public class HttpUtils {

    public static String httpGet(String url, List<NameValuePair> list) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        URIBuilder uriBuilder;
        try {
            uriBuilder = new URIBuilder(url);
            uriBuilder.setParameters(list);
            HttpGet get = new HttpGet(uriBuilder.build());
            CloseableHttpResponse response = client.execute(get);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            log.error("httpGet=>调用失败:", e);
        }
        return "";
    }

}
