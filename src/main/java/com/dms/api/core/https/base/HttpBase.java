package com.dms.api.core.https.base;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * HttpClient Base
 */
public class HttpBase {

    private final static String UTF8 = "UTF-8";
    private final static String EMPTY_STR = "";
    private final static int SOCKETTIMEOUT = 60000;      // 从连接池中获取到连接的最长时间
    private final static int CONNECTTIMEOUT = 60000;     // 创建连接的最长时间
    private final static int MAXTOTAL = 200;             // 最大链接数
    private final static int MAXPERROUTE = 20;           // 设置每个主机地址的并发数
    protected final static Boolean LOG_PRINT = true;     // 请求成功的日志是否输出
    private static PoolingHttpClientConnectionManager poolConnManager;

    /**
     * 获取HttpClient
     */
    private static CloseableHttpClient getHttpClient() {
        //初始化连接池
        if (poolConnManager == null) {
            poolConnManager = new PoolingHttpClientConnectionManager(); // 整个连接池最大连接数
            poolConnManager.setMaxTotal(MAXTOTAL);
            poolConnManager.setDefaultMaxPerRoute(MAXPERROUTE); // 每路由最大连接数，默认值是2
        }

        return HttpClients.custom()
                          .setConnectionManager(poolConnManager)
                          .setConnectionManagerShared(true)
                          .build();
    }

    /**
     * 请求设置
     */
    private static RequestConfig setRequestConfig() {

        return RequestConfig.custom()
                .setSocketTimeout(SOCKETTIMEOUT)
                .setConnectTimeout(CONNECTTIMEOUT)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .build();
    }

    /**
     * url参数拼接
     */
    private static String buildUrl(String url, Map<String, ?> params) throws UnsupportedEncodingException {
        if(params == null){
            return url;
        }

        StringBuilder sbUrl = new StringBuilder();
        StringBuilder sbQuery = new StringBuilder();

        sbUrl.append(url);
        for (Map.Entry<String, ?> query : params.entrySet()) {
            if (0 < sbQuery.length()) {
                sbQuery.append("&");
            }
            if (StringUtils.isBlank(query.getKey())
                    && !StringUtils.isBlank(query.getValue().toString())) {
                sbQuery.append(query.getValue());
            }
            if (!StringUtils.isBlank(query.getKey())) {
                sbQuery.append(query.getKey());
                if (!StringUtils.isBlank(query.getValue().toString())) {
                    sbQuery.append("=");
                    sbQuery.append(URLEncoder.encode(query.getValue().toString(), UTF8));
                }
            }
        }
        if (0 < sbQuery.length()) {
            sbUrl.append("?").append(sbQuery);
        }

        return sbUrl.toString();
    }

    /**
     * doGet
     */
    protected static String doGet(final String url, final Map<String, ?> headers, final Map<String, ?> params) throws Exception {
        StringBuilder buffer = new StringBuilder();
        HttpClient httpClient = new HttpClient();
        httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);    // 设置header头

        GetMethod getMethod = new GetMethod(buildUrl(url, params));
        int statusCode = httpClient.executeMethod(getMethod);
        if (statusCode == 200) {
            InputStream stream = getMethod.getResponseBodyAsStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream,UTF8));
            String result = "";
            while ((result = reader.readLine()) != null ){
                buffer.append(result);
            }
        }
        return buffer.toString();
    }

    /**
     * doPost
     */
    protected static String doPost(final String url, final Map<String, ?> headers, final Map<String, ?> params) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setConfig(setRequestConfig());
            for (Map.Entry<String, ?> param : headers.entrySet()) {
                httpPost.addHeader(param.getKey(), (String) param.getValue());
            }
            httpPost.setEntity(new StringEntity(JSONObject.toJSONString(params), UTF8));

            try (CloseableHttpClient httpClient = getHttpClient();
                 CloseableHttpResponse response = httpClient.execute(httpPost)){

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity);
                }
            }
            return EMPTY_STR;
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * doPostC
     */
    protected static String doPostC(String url, Map<String, String> map) throws IOException {
        String body = "";
        try (CloseableHttpClient client = HttpClients.createDefault()){
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(setRequestConfig());
            List<NameValuePair> nvps = new ArrayList<>();
            if (map != null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, UTF8));
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            CloseableHttpResponse response = client.execute(httpPost);  //执行请求操作，并拿到结果（同步阻塞）
            HttpEntity entity = response.getEntity();
            if (entity != null) body = EntityUtils.toString(entity, UTF8);
            EntityUtils.consume(entity);
            response.close();
        }
        return body;
    }

}
