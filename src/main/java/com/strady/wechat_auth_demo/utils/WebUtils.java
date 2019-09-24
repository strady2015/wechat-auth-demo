package com.strady.wechat_auth_demo.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @Author: strady
 * @Date: 2019-09-23
 * @Time: 18:28:55
 * @Description: Web工具类
 */
public class WebUtils {

    private static final Log _LOG = LogFactory.getLog(WebUtils.class);


    /**
     * @param request HttpServletRequest对象
     * @return 获取当前站点基准URL
     */
    public static String baseURL(HttpServletRequest request) {
        StringBuilder basePath = new StringBuilder(request.getScheme()).append("://").append(request.getServerName());
        if (request.getServerPort() != 80 && request.getServerPort() != 443) {
            basePath.append(":").append(request.getServerPort());
        }
        if (StringUtils.isNotBlank(request.getContextPath())) {
            basePath.append(request.getContextPath());
        }
        if (!basePath.toString().endsWith("/")) {
            basePath.append("/");
        }
        return basePath.toString();
    }

    public static String encodeURL(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String decodeURL(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }


    /**
     * @param request HttpServletRequest对象
     * @return 是否AJAX请求（需要在使用Ajax请求时设置请求头）
     */
    public static boolean isAjax(HttpServletRequest request) {
        // 判断条件: (x-requested-with = XMLHttpRequest)
        String _httpx = request.getHeader("x-requested-with");
        return StringUtils.isNotBlank(_httpx) && "XMLHttpRequest".equalsIgnoreCase(_httpx);
    }

    public static boolean isAjax(HttpServletRequest request, boolean ifJson, boolean ifXml) {
        if (isAjax(request)) {
            return true;
        }
        if (ifJson || ifXml) {
            String _format = StringUtils.trimToNull(request.getParameter("format"));
            if (ifJson && StringUtils.equalsIgnoreCase(_format, "json")) {
                return true;
            }
            if (ifXml && StringUtils.equalsIgnoreCase(_format, "xml")) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param request HttpServletRequest对象
     * @return 判断当前请求是否采用POST方式提交
     */
    public static boolean isPost(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod());
    }


    /**
     * @param request HttpServletRequest对象
     * @return 获取用户IP地址(当存在多个IP地址时仅返回第一个)
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String[] _ips = getRemoteAddrs(request);
        if (_ips != null && _ips.length > 0) {
            return _ips[0];
        }
        return null;
    }

    /**
     * @param request HttpServletRequest对象
     * @return 获取用户IP地址(以数组的形式返回所有IP)
     */
    public static String[] getRemoteAddrs(HttpServletRequest request) {
        String _ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(_ip) || "unknown".equalsIgnoreCase(_ip)) {
            _ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(_ip) || "unknown".equalsIgnoreCase(_ip)) {
            _ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(_ip) || "unknown".equalsIgnoreCase(_ip)) {
            _ip = request.getRemoteAddr();
            if (StringUtils.equals(_ip, "127.0.0.1")) {
                _ip = StringUtils.join(NetworkUtils.IP.getHostIPAddrs(), ",");
            }
        }
        return StringUtils.split(_ip, ',');
    }


}
