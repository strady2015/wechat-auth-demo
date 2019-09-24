package com.strady.wechat_auth_demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.strady.wechat_auth_demo.utils.ICode;
import com.strady.wechat_auth_demo.utils.ParamsUtils;
import com.strady.wechat_auth_demo.utils.Result;
import com.strady.wechat_auth_demo.utils.WebUtils;
import com.strady.wechat_auth_demo.utils.http.HttpHelpers;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: strady
 * @Date: 2019-09-23
 * @Time: 18:27:47
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping(value = "/oauth")
public class OauthController {

    private static Logger logger = LoggerFactory.getLogger(OauthController.class);

    @Autowired
    private HttpHelpers httpHelper;

    private String CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?";
    private String TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?";
    private String INFO_URL = "https://api.weixin.qq.com/sns/userinfo?";

    private String APP_ID = "wx78cab1588a1d84ab";
    private String APP_SECRET = "9755cfc2ac53d74d0c1ef086e0d96569";
    private String RESPONSE_TYPE = "code";
    private String GRANT_TYPE = "authorization_code";
    private String WECHAT_REDIRECT = "#wechat_redirect";

    @RequestMapping(value = "/index")
    public void oauth(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("appid", APP_ID);
        params.put("redirect_uri", URLEncoder.encode(WebUtils.baseURL(request) + "oauth/redirect", HttpHelpers.DEFAULT_CHARSET));
        params.put("response_type", RESPONSE_TYPE);
        params.put("scope", "snsapi_userinfo");
        params.put("state", "state" + WECHAT_REDIRECT);
        String url = CODE_URL.concat(ParamsUtils.buildQueryParamStr(params, false, HttpHelpers.DEFAULT_CHARSET));
        response.sendRedirect(url);
    }

    @RequestMapping(value = "/redirect")
    public Result oauth(@RequestParam String code) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("appid", APP_ID);
        params.put("secret", APP_SECRET);
        params.put("code", code);
        params.put("grant_type", GRANT_TYPE);
        String result = httpHelper.doGet(TOKEN_URL, params);
        JSONObject jsonObject = JSON.parseObject(result);

        String token = jsonObject.getString("access_token");
        String openId = jsonObject.getString("openid");

        logger.info("token: " + token);
        logger.info("openId: " + openId);
        JSONObject user = getUser(token, openId);
        return Result.builder().code(ICode.Code.SUCCESS).msg(ICode.Code.Msg.__ERROR_CODES.get(ICode.Code.SUCCESS)).attr("user", user);
    }


    public JSONObject getUser(String token, String openId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("access_token", token);
        params.put("openid", openId);
        params.put("lang", "zh_CN");
        String result = httpHelper.doGet(INFO_URL, params);
        return JSON.parseObject(result);
    }
}
