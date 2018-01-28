package com.hklh8.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by GouBo on 2018/1/28.
 */
public class ResponseUtils {

    public static JSONObject formatError(String code, String msg, String ex) {
        JSONObject error = new JSONObject();
        error.put("error_code", code);
        error.put("error_msg", msg);
        error.put("exception", ex);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", error);
        return jsonObject;
    }

}
