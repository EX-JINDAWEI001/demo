package com.example.demo.common.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private static final String DEFAULT_CHARSET = "UTF-8";

    @Autowired
    private HttpClientFactory httpClientFactory;

    private int connTimeout;

    private int connRequestTimeout;

    private int socketTimeout;

    public String httpGetRequest(CloseableHttpClient httpClient, String baseUrl, Map<String, String> params){
        if (httpClient == null){
            throw new NullPointerException("httpGetRequest parameter httpClient is null");
        }
        if(StringUtils.isEmpty(baseUrl)){
            throw new NullPointerException("httpGetRequest parameter baseUrl is null");
        }
        String url = baseUrl;
        if(params != null && params.size() > 0){
            url += "?" + buildGetParams(params);
        }
        logger.info("httpGetRequest baseUrl is:{}", baseUrl);
        long startTime = System.currentTimeMillis();
        try {
            HttpGet get = new HttpGet(url);
            CloseableHttpResponse respons = httpClient.execute(get);
            logger.info("httpGetRequest statusCode:{}", respons.getStatusLine().getStatusCode());
            String result = EntityUtils.toString(respons.getEntity(), DEFAULT_CHARSET);
            logger.info("httpGetRequest result:{}", result);
            return result;
        } catch (ClientProtocolException e) {
            logger.error("httpGetRequest to url:{} is failed, ClientProtocolException is:{}", url, e);
        } catch (IOException e) {
            logger.error("httpGetRequest to url:{} is failed, IOException is:{}", url, e);
        } finally {
            logger.info("httpGetRequest costTime is {}", System.currentTimeMillis() - startTime);
        }
        return null;
    }

    private String buildGetParams(Map<String, String> params) {
        if (params != null){
            List<NameValuePair> nvps = new ArrayList<>();
            for (Map.Entry<String, String> e : params.entrySet()){
                String name = e.getKey();
                String value = e.getValue();
                nvps.add(new BasicNameValuePair(name, value));
            }
            return URLEncodedUtils.format(nvps, DEFAULT_CHARSET);
        }
        return "";
    }

    public String httpGetRequest(CloseableHttpClient httpClient, String url){
        return httpGetRequest(httpClient, url, null);
    }

    public String httpGetRequest(String baseUrl, Map<String, String> params){
        return httpGetRequest(buildHttpClient(), baseUrl, params);
    }

    public String httpGetRequest(String url){
        return httpGetRequest(buildHttpClient(), url, null);
    }

    //================post method=================\\

    public String httpPostRequest(CloseableHttpClient httpClient, String url, Map<String, String> params){
        return httpPostRequest(httpClient, url, params, null);
    }

    public String httpPostRequest(String url, Map<String, String> params, Map<String, String> headers){
        return httpPostRequest(buildHttpClient(), url, params, headers);
    }

    public String httpPostRequest(String url, Map<String, String> params){
        return httpPostRequest(buildHttpClient(), url, params, null);
    }

    public String httpPostJsonRequest(CloseableHttpClient httpClient, String url, String jsonStr){
        return httpPostJsonRequest(httpClient, url, jsonStr, null);
    }

    public String httpPostJsonRequest(String url, String jsonStr, Map<String, String> headers){
        return httpPostJsonRequest(buildHttpClient(), url, jsonStr, headers);
    }

    public String httpPostJsonRequest(String url, String jsonStr){
        return httpPostJsonRequest(buildHttpClient(), url, jsonStr, null);
    }

    public String httpPostRequest(CloseableHttpClient httpClient, String url, Map<String, String> params, Map<String, String> headers){
        if(httpClient == null){
            throw new NullPointerException("httpPostRequest parameter httpClient is null");
        }
        if(StringUtils.isEmpty(url)){
            throw new NullPointerException("httpPostRequest parameter url is null");
        }
        logger.info("httpPostRequest parameter url is {}, params is {}, headers is {}", url, params, headers);
        long startTime = System.currentTimeMillis();
        try{
            HttpPost post = new HttpPost(url);
            setPostHeaders(post, headers);
            buildPostParams(post, params);
            CloseableHttpResponse response = httpClient.execute(post);
            logger.info("httpPostRequest statusCode:{}", response.getStatusLine().getStatusCode());
            String result = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
            logger.info("httpPostRequest result:{}", result);
            return result;
        } catch (ClientProtocolException e) {
            logger.error("httpPostRequest to url:{} is failed, ClientProtocolException is:{}", url, e);
        } catch (IOException e) {
            logger.error("httpPostRequest to url:{} is failed, IOException is:{}", url, e);
        } finally {
            logger.info("httpPostRequest costTime is {}", System.currentTimeMillis() - startTime);
        }
        return null;
    }

    public String httpPostJsonRequest(CloseableHttpClient httpClient, String url, String jsonStr, Map<String, String> headers){
        if(httpClient == null){
            throw new NullPointerException("httpPostJsonRequest parameter httpClient is null");
        }
        if(StringUtils.isEmpty(url)){
            throw new NullPointerException("httpPostJsonRequest parameter url is null");
        }
        logger.info("httpPostJsonRequest parameter url is {}, jsonStr is {}, headers is {}", url, jsonStr, headers);
        long startTime = System.currentTimeMillis();
        try{
            HttpPost post = new HttpPost(url);
            setPostHeaders(post, headers);
            post.setEntity(new StringEntity(jsonStr, ContentType.APPLICATION_JSON));
            CloseableHttpResponse response = httpClient.execute(post);
            logger.info("httpPostJsonRequest statusCode:{}", response.getStatusLine().getStatusCode());
            String result = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
            logger.info("httpPostJsonRequest result:{}", result);
            return result;
        } catch (ClientProtocolException e) {
            logger.error("httpPostJsonRequest to url:{} is failed, ClientProtocolException is:{}", url, e);
        } catch (IOException e) {
            logger.error("httpPostJsonRequest to url:{} is failed, IOException is:{}", url, e);
        } finally {
            logger.info("httpPostJsonRequest costTime is {}", System.currentTimeMillis() - startTime);
        }
        return null;
    }

    private void setPostHeaders(HttpPost post, Map<String, String> headers) {
        if (headers != null){
            List<NameValuePair> nvps = new ArrayList<>();
            for (Map.Entry<String, String> e : headers.entrySet()){
                String name = e.getKey();
                String value = e.getValue();
                post.setHeader(new BasicHeader(name, value));
            }
        }
    }

    private void buildPostParams(HttpPost post, Map<String, String> params) throws UnsupportedEncodingException {
        if (params != null){
            List<NameValuePair> nvps = new ArrayList<>();
            for (Map.Entry<String, String> e : params.entrySet()){
                String name = e.getKey();
                String value = e.getValue();
                nvps.add(new BasicNameValuePair(name, value));
            }
            post.setEntity(new UrlEncodedFormEntity(nvps, DEFAULT_CHARSET));
        }
    }

    private CloseableHttpClient buildHttpClient() {
        if (connTimeout > 0 && connRequestTimeout > 0 && socketTimeout > 0){
            return this.getClientFactory().build(connTimeout, connRequestTimeout, socketTimeout);
        } else {
            return this.getClientFactory().build();
        }
    }

    private HttpClientFactory getClientFactory(){
        if(httpClientFactory == null){
            httpClientFactory = new HttpClientFactory();
        }
        return httpClientFactory;
    }

    public HttpClientFactory getHttpClientFactory() {
        return httpClientFactory;
    }

    public void setHttpClientFactory(HttpClientFactory httpClientFactory) {
        this.httpClientFactory = httpClientFactory;
    }

    public int getConnTimeout() {
        return connTimeout;
    }

    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public int getConnRequestTimeout() {
        return connRequestTimeout;
    }

    public void setConnRequestTimeout(int connRequestTimeout) {
        this.connRequestTimeout = connRequestTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

}
