package com.strady.wechat_auth_demo.utils;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: strady
 * @Date: 2019-09-23
 * @Time: 18:43:09
 * @Description: 通用返回结果类
 */
@Data
public class Result {

    private Integer code;
    private String msg;
    private Long count;
    private List data = new ArrayList();
    private Map<Object, Object> attrs = new HashMap<Object, Object>();


    public static Result builder() {
        return new Result();
    }

    public int code() {
        return code;
    }

    public Result code(Integer code) {
        this.code = code;
        return this;
    }

    public Long count() {
        return count;
    }

    public Result count(Long count) {
        this.count = count;
        return this;
    }

    public String msg() {
        return StringUtils.trimToEmpty(msg);
    }

    public Result msg(String msg) {
        this.msg = msg;
        return this;
    }


    public Result attr(Object key,Object value) {
        this.attrs.put(key,value);
        return this;
    }

    public Result data(Object dataValue) {
        data = (List) dataValue;
        return this;
    }

}
