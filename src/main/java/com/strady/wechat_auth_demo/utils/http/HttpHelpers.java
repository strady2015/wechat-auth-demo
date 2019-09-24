package com.strady.wechat_auth_demo.utils.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: strady
 * @Date: 2019-09-23
 * @Time: 18:35:39
 * @Description:
 */
@Slf4j
@Component
public class HttpHelpers {

    //    @Autowired
    private CloseableHttpClient httpClient = HttpClients.createDefault();

    //    @Autowired
    private RequestConfig config = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(1000).build();

    // 编码方式
    public static final String DEFAULT_CHARSET = "UTF-8";


    private static List<NameValuePair> doBuildNameValuePairs(Map<String, Object> params) {
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() != null && StringUtils.isNotBlank(entry.getValue().toString())) {
                nameValuePair.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
        }
        return nameValuePair;
    }

    /**
     * 不带参数的get请求
     *
     * @param url
     * @return
     * @throws Exception
     */
    public String doGet(String url) throws Exception {
        return this.doGet(url, null);
    }

    /**
     * 带参数的get请求
     *
     * @param url
     * @param map
     * @return
     * @throws Exception
     */
    public String doGet(String url, Map<String, Object> map) throws Exception {
        try {
            log.info("请求 URL [ " + url + "]");
            RequestBuilder _request = RequestBuilder.get().setUri(url);
            _request.setConfig(config);
            if (map != null) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    _request.addParameter(entry.getKey(), entry.getValue().toString());
                }
            }
            CloseableHttpResponse response = this.httpClient.execute(_request.build());
            String result = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
            log.info("请求 URL [ " + url + "] 返回报文 [" + result + "] ");
            return result;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        } finally {
            //this.httpClient.close();
        }
    }

    /**
     * 带参数的post请求
     *
     * @param url
     * @param map
     * @return
     * @throws Exception
     */
    public String doPost(String url, Map<String, Object> map) throws Exception {
        log.info("请求 URL [ " + url + "]");
        try {
            RequestBuilder _request = RequestBuilder.post().setUri(url);
            // 加入配置信息
            _request.setConfig(config);
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(doBuildNameValuePairs(map), DEFAULT_CHARSET);
            // 把表单放到post里
            _request.setEntity(urlEncodedFormEntity);
            // 发起请求
            CloseableHttpResponse response = this.httpClient.execute(_request.build());
            String result = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
            log.info("请求 URL [ " + url + "] 返回报文 [" + result + "]");
            return result;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        } finally {
            //this.httpClient.close();
        }

    }

    /**
     * 不带参数post请求
     *
     * @param url
     * @return
     * @throws Exception
     */
    public String doPost(String url) throws Exception {
        return this.doPost(url, null);
    }
}
