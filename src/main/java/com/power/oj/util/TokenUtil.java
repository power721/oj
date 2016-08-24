package com.power.oj.util;

import com.alibaba.fastjson.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenUtil {

    private TokenUtil() {
    }

    /**
     * 参考自 qq sdk
     *
     * @param @param string @param @return 设定文件 @return String 返回类型 @throws
     */
    public static String getAccessToken(String string) {
        String accessToken = "";
        try {
            JSONObject json = JSONObject.parseObject(string);
            if (null != json) {
                accessToken = json.getString("access_token");
            }
        } catch (Exception e) {
            Matcher m = Pattern.compile("^access_token=(\\w+)&expires_in=(\\w+)&refresh_token=(\\w+)$").matcher(string);
            if (m.find()) {
                accessToken = m.group(1);
            } else {
                Matcher m2 = Pattern.compile("^access_token=(\\w+)&expires_in=(\\w+)$").matcher(string);
                if (m2.find()) {
                    accessToken = m2.group(1);
                }
            }
        }
        return accessToken;
    }

    /**
     * 匹配openid
     *
     * @param @param string @param @return 设定文件 @return String 返回类型 @throws
     */
    public static String getOpenId(String string) {
        String openid = null;
        Matcher m = Pattern.compile("\"openid\"\\s*:\\s*\"(\\w+)\"").matcher(string);
        if (m.find()) {
            openid = m.group(1);
        }
        return openid;
    }

    /**
     * sina uid于qq分离
     *
     * @param @param  string
     * @param @return 设定文件
     * @return String 返回类型
     * @throws @Title: getUid
     */
    public static String getUid(String string) {
        JSONObject json = JSONObject.parseObject(string);
        return json.getString("uid");
    }
}
