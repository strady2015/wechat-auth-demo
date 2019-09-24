package com.strady.wechat_auth_demo.utils;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;

/**
 * @Author: strady
 * @Date: 2019-09-23
 * @Time: 18:41:52
 * @Description: 参数工具类
 */
public class ParamsUtils {
    /**
     * @param params  请求参数映射
     * @param encode  是否对参数进行编码
     * @param charset Encode编码字符集，默认UTF-8
     * @return 对参数进行ASCII正序排列并生成请求参数串
     */
    public static String buildQueryParamStr(Map<String, ?> params, boolean encode, String charset) {
        String[] keys = params.keySet().toArray(new String[params.size()]);
        Arrays.sort(keys);
        StringBuilder paramStr = new StringBuilder();
        for (String key : keys) {
            Object value = params.get(key);
            if (value != null) {
                if (value.getClass().isArray()) {
                    for (Object v : (Object[]) value) {
                        appendParams(key, v, paramStr, encode, charset);
                    }
                } else {
                    appendParams(key, value, paramStr, encode, charset);
                }
            }
        }
        return paramStr.toString();
    }

    private static void appendParams(String key, Object value, StringBuilder paramStr, boolean encode, String charset) {
        if (paramStr.length() > 0) {
            paramStr.append("&");
        }
        String valueStr = value.toString();
        if (encode) {
            try {
                paramStr.append(key).append("=").append(URLEncoder.encode(valueStr, StringUtils.defaultIfBlank(charset, "UTF-8")));
            } catch (UnsupportedEncodingException e) {
            }
        } else {
            paramStr.append(key).append("=").append(valueStr);
        }
    }

}
