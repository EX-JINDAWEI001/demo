package com.example.demo.common.http;

import org.apache.http.Consts;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.CodingErrorAction;
import java.util.Arrays;

@Configuration
public class HttpClientFactory implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientFactory.class);

    private static int DEFAULT_CONN_TIMEOUT = 20 *1000;

    private static int DEFAULT_CONN_REQUEST_TIMEOUT = 20 *1000;

    private static int DEFAULT_SOCKET_TIMEOUT = 60 *1000;

    private static int maxTotal = 100;

    private static int maxConnPerRout = 20;

    private PoolingHttpClientConnectionManager connManager;

    @Override
    public void afterPropertiesSet() {
        this.buildConnManager();
    }

    private void buildConnManager() {
        logger.info("=====初始化http连接池=====");
        HttpMessageParserFactory<HttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory();
        HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();

        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory
                = new ManagedHttpClientConnectionFactory(requestWriterFactory, responseParserFactory);

        //create a connection manager with custom configuration
        connManager = new PoolingHttpClientConnectionManager(connFactory);

        //create a message constraints
        MessageConstraints messageConstraints = MessageConstraints.custom()
                .setMaxHeaderCount(200).setMaxLineLength(2000).build();

        //create connection configuration
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8)
                .setMessageConstraints(messageConstraints).build();

        //configure the connection manager to use connection configuration either by default or for a specific host
        connManager.setDefaultConnectionConfig(connectionConfig);
        connManager.setDefaultMaxPerRoute(maxConnPerRout);
        connManager.setMaxTotal(maxTotal);
    }

    public CloseableHttpClient build(){
        return build(DEFAULT_CONN_TIMEOUT, DEFAULT_CONN_REQUEST_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    public CloseableHttpClient build(int connTimeout){
        return build(connTimeout, DEFAULT_CONN_REQUEST_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    public CloseableHttpClient build(int connTimeout, int reqTimeout, int socketTimeout){
        if(connManager == null){
            buildConnManager();
        }

        //create global request configuration
        RequestConfig defaultRequestConfig = RequestConfig.custom().setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .setSocketTimeout(socketTimeout).setConnectTimeout(connTimeout).setConnectionRequestTimeout(reqTimeout).build();

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager)
                .setDefaultRequestConfig(defaultRequestConfig).build();

        return httpClient;
    }


}
