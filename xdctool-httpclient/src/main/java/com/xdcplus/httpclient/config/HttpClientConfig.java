package com.xdcplus.httpclient.config;

import com.xdcplus.spring.boot.autoconfigure.HttpClientPoolProperties;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * restTemplate ??????
 * @author Rong.Jia
 * @date 2019/12/28 21:05
 */
@Configuration
@EnableConfigurationProperties({HttpClientPoolProperties.class})
@ConditionalOnClass(value = {RestTemplate.class, CloseableHttpClient.class})
public class HttpClientConfig implements ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(HttpClientConfig.class);

    private final HttpClientPoolProperties httpClientPoolProperties;
    private ApplicationContext applicationContext;
    public HttpClientConfig(HttpClientPoolProperties httpClientPoolProperties) {
        this.httpClientPoolProperties = httpClientPoolProperties;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * ??????HTTP???????????????
     */
    @Bean(name = "clientHttpRequestFactory")
    public ClientHttpRequestFactory clientHttpRequestFactory() {

        // maxTotalConnection ??? maxConnectionPerRoute ????????????
        if (httpClientPoolProperties.getMaxTotalConnect() <= 0) {
            throw new IllegalArgumentException("invalid maxTotalConnection: " + httpClientPoolProperties.getMaxTotalConnect());
        }
        if (httpClientPoolProperties.getMaxConnectPerRoute() <= 0) {
            throw new IllegalArgumentException("invalid maxConnectionPerRoute: " + httpClientPoolProperties.getMaxConnectPerRoute());
        }
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient());

        // ????????????
        clientHttpRequestFactory.setConnectTimeout(httpClientPoolProperties.getConnectTimeout());

        // ??????????????????????????????SocketTimeout
        clientHttpRequestFactory.setReadTimeout(httpClientPoolProperties.getReadTimeout());

        // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        clientHttpRequestFactory.setConnectionRequestTimeout(httpClientPoolProperties.getConnectionRequestTimout());

        return clientHttpRequestFactory;
    }

    /**
     * ?????????RestTemplate,?????????spring???Bean????????????spring????????????
     */
    @Bean(name = "httpClientTemplate")
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return createRestTemplate(factory);
    }

    /**
     * ??????httpClient
     *
     * @return HttpClient
     */
    @Bean
    public HttpClient httpClient() {

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        try {

            //????????????ssl??????
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (arg0, arg1) -> true).build();

            httpClientBuilder.setSSLContext(sslContext);
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    // ??????http???https??????
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslConnectionSocketFactory).build();

            //??????Httpclient????????????????????????(??????)???????????????netty???okHttp????????????http??????
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // ???????????????
            poolingHttpClientConnectionManager.setMaxTotal(httpClientPoolProperties.getMaxTotalConnect());
            // ??????????????????
            poolingHttpClientConnectionManager.setDefaultMaxPerRoute(httpClientPoolProperties.getMaxConnectPerRoute());
            //???????????????
            httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
            // ????????????
            httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(httpClientPoolProperties.getRetryTimes(), true));

            //?????????????????????
            List<Header> headers = getDefaultHeaders();
            httpClientBuilder.setDefaultHeaders(headers);
            //???????????????????????????
            httpClientBuilder.setKeepAliveStrategy(connectionKeepAliveStrategy());
            return httpClientBuilder.build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            log.error("?????????HTTP??????????????? {}", e.getMessage());
        }
        return null;
    }


    /**
     * ???????????????????????????
     * @return  ?????????????????????
     */
    public ConnectionKeepAliveStrategy connectionKeepAliveStrategy(){
        return (response, context) -> {
            // Honor 'keep-alive' header
            HeaderElementIterator it = new BasicHeaderElementIterator(
                    response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (it.hasNext()) {
                HeaderElement he = it.nextElement();
                String param = he.getName();
                String value = he.getValue();
                if (value != null && "timeout".equalsIgnoreCase(param)) {
                    try {
                        return Long.parseLong(value) * 1000;
                    } catch(NumberFormatException e) {
                        log.error("????????????????????????????????? {}", e.getMessage());
                    }
                }
            }
            HttpHost target = (HttpHost) context.getAttribute(
                    HttpClientContext.HTTP_TARGET_HOST);
            //????????????????????????,????????????????????????????????????,???????????????
            Optional<Map.Entry<String, Integer>> any = Optional.ofNullable(httpClientPoolProperties.getKeepAliveTargetHost()).orElseGet(HashMap::new)
                    .entrySet().stream().filter(
                            e -> e.getKey().equalsIgnoreCase(target.getHostName())).findAny();
            //???????????????????????????????????????
            return any.map(en -> en.getValue() * 1000L).orElse(httpClientPoolProperties.getKeepAliveTime() * 1000L);
        };
    }

    /**
     * ???????????????
     *
     * @return ?????????
     */
    private List<Header> getDefaultHeaders() {

        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
        headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
        headers.add(new BasicHeader("Accept-Language", "zh-CN"));
        headers.add(new BasicHeader("Connection", "Keep-Alive"));
        return headers;
    }


    /**
     * ?????? RestTemplate
     *
     * @param factory HttpClient??????
     * @return {@link RestTemplate} RestTemplate
     */
    private RestTemplate createRestTemplate(ClientHttpRequestFactory factory) {

        RestTemplate restTemplate = new RestTemplate(factory);

        //????????????RestTemplate?????????MessageConverter
        //????????????StringHttpMessageConverter????????????????????????????????????
        modifyDefaultCharset(restTemplate);

        //??????????????????interceptor?????????
        try {
            Map<String, ClientHttpRequestInterceptor> requestInterceptorMap = applicationContext.getBeansOfType(ClientHttpRequestInterceptor.class);
            if (CollectionUtil.isNotEmpty(requestInterceptorMap)) {
                restTemplate.setInterceptors(CollectionUtil.newArrayList(requestInterceptorMap.values()));
            }
        }catch (Exception e) {
            log.error("Failed to configure interceptor {}", e.getMessage());
        }

        //?????????????????????
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());

        return restTemplate;
    }

    /**
     * ?????????????????????????????????utf-8
     *
     * @param restTemplate RestTemplate ??????
     */
    private void modifyDefaultCharset(RestTemplate restTemplate) {
        List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
        HttpMessageConverter<?> converterTarget = null;
        for (HttpMessageConverter<?> item : converterList) {
            if (StringHttpMessageConverter.class == item.getClass()) {
                converterTarget = item;
                break;
            }
        }
        if (null != converterTarget) {
            converterList.remove(converterTarget);
        }
        Charset defaultCharset = Charset.forName(httpClientPoolProperties.getCharset());
        converterList.add(1, new StringHttpMessageConverter(defaultCharset));
        converterList.add(converterList.size() -1, fastJsonHttpMessageConverters());
    }

    /**
     * RestTemplate ???????????????
     * @return  ???????????????
     */
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverters() {

        // 1.????????????converters?????????????????????
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        // 2.??????fastjson????????????????????????: ??????????????????????????????json??????
        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        //Long?????????String??????
//        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
//        // ToStringSerializer ???????????? com.alibaba.fastjson.serializer.ToStringSerializer
//        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
//        serializeConfig.put(Long.class, ToStringSerializer.instance);
//        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
//        fastJsonConfig.setSerializeConfig(serializeConfig);

        fastJsonConfig.setSerializerFeatures(SerializerFeature.QuoteFieldNames,
                SerializerFeature.WriteEnumUsingToString,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.DisableCircularReferenceDetect
        );

        // 3.???converter?????????????????????
        fastConverter.setFastJsonConfig(fastJsonConfig);
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);

        // 4.??????HttpMessageConverters??????
        return fastConverter;
    }












}
