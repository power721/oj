package com.power.oj.api.oauth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.power.oj.core.OjConfig;
import com.power.oj.util.TokenUtil;

public class SinaOauth extends Oauth
{
  private static final String AUTH_URL = "https://api.weibo.com/oauth2/authorize";
  private static final String TOKEN_URL = "https://api.weibo.com/oauth2/access_token";
  private static final String TOKEN_INFO_URL = "https://api.weibo.com/oauth2/get_token_info";
  private static final String USER_INFO_URL = "https://api.weibo.com/2/users/show.json";
  private static final String REDIRECT_URI = "api/oauth/sina/callback";

  public SinaOauth()
  {
    super();
    setClientId(OjConfig.getString("openid_sina"));
    setClientSecret(OjConfig.getString("openkey_sina"));
    setRedirectUri(OjConfig.getBaseUrl() + "/" + REDIRECT_URI);
  }

  public String getAuthorizeUrl()
  {
    Map<String, String> params = new HashMap<String, String>();
    params.put("state", "sina");
    params.put("response_type", "code");
    params.put("client_id", getClientId());
    params.put("redirect_uri", getRedirectUri());
    return super.getAuthorizeUrl(AUTH_URL, params);
  }

  public String getTokenByCode(String code) throws IOException
  {
    Map<String, String> params = new HashMap<String, String>();
    params.put("code", code);
    params.put("client_id", getClientId());
    params.put("client_secret", getClientSecret());
    params.put("grant_type", "authorization_code");
    params.put("redirect_uri", getRedirectUri());
    return TokenUtil.getAccessToken(super.doPost(TOKEN_URL, params));
  }

  public String getTokenInfo(String accessToken) throws IOException
  {
    Map<String, String> params = new HashMap<String, String>();
    params.put("access_token", accessToken);
    return TokenUtil.getUid(super.doPost(TOKEN_INFO_URL, params));
  }

  public String getUserInfo(String accessToken, String uid) throws IOException
  {
    Map<String, String> params = new HashMap<String, String>();
    params.put("uid", uid);
    params.put("access_token", accessToken);
    return super.doGet(USER_INFO_URL, params);
  }

  @SuppressWarnings("unchecked")
  public Map<String, String> getUserInfoByCode(String code) throws IOException
  {
    String accessToken = getTokenByCode(code);
    String uid = getTokenInfo(accessToken);
    Map<String, String> dataMap = JSON.parseObject(getUserInfo(accessToken, uid), Map.class);
    dataMap.put("openid", uid);
    return dataMap;
  }
}
