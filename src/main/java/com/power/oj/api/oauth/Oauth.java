package com.power.oj.api.oauth;

import com.power.oj.util.HttpUtil;

import java.io.IOException;
import java.util.Map;

/**
 * Oauth BAE
 *
 * @author L.cm email: 596392912@qq.com site: http://www.dreamlu.net
 * @date Jun 24, 2013 9:58:25 PM
 */
public class Oauth {
    private final static String DEFAULTCHAESET = "UTF-8";

    private String clientId;
    private String clientSecret;
    private String redirectUri;

    public Oauth() {
    }

    public String getAuthorizeUrl(String authorize, Map<String, String> params) {
        return getAuthorizationCode(authorize, params);
    }

    private String getAuthorizationCode(String authorize, Map<String, String> basic) {
        String query = HttpUtil.buildQuery(basic, DEFAULTCHAESET);
        return authorize + "?" + query;
    }

    public String doPost(String url, Map<String, String> params) throws IOException {
        return HttpUtil.doPost(url, params);
    }

    public String doGet(String url, Map<String, String> params) throws IOException {
        return HttpUtil.doGet(url, params);
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

}
