package com.cn.service.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.util.List;

public class HttpUtils {

    private static final Logger LOGGER = Logger.getLogger(HttpUtils.class);

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
            LOGGER.error("httpGet=>调用失败:", e);
        }
        return "";
    }

}
