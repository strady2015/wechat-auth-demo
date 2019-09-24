package com.strady.wechat_auth_demo.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: strady
 * @Date: 2019-09-23
 * @Time: 18:46:02
 * @Description: 通用错误码
 */
public interface ICode {

    //错误码描述
    class Code {
        public final static Integer SUCCESS = 0;//成功
        public final static Integer FAIL = -10;//失败
        public final static Integer NO_RESULT = 50000;//查询无结果

        public static class Msg {
            public static Map<Integer, String> __ERROR_CODES = new HashMap<Integer, String>();

            static {
                __ERROR_CODES.put(Code.SUCCESS, "操作成功");
                __ERROR_CODES.put(Code.FAIL, "操作失败");
                __ERROR_CODES.put(Code.NO_RESULT, "查询无结果");
            }
        }
    }
}
