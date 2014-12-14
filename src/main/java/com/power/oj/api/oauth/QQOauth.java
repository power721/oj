package com.power.oj.api.oauth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.jfinal.log.Logger;
import com.power.oj.core.OjConfig;
import com.power.oj.util.TokenUtil;

public class QQOauth extends Oauth {
	private static final Logger log = Logger.getLogger(QQOauth.class);

	private static final String AUTH_URL = "https://graph.qq.com/oauth2.0/authorize";
	private static final String TOKEN_URL = "https://graph.qq.com/oauth2.0/token";
	private static final String TOKEN_INFO_URL = "https://graph.qq.com/oauth2.0/me";
	private static final String USER_INFO_URL = "https://graph.qq.com/user/get_user_info";
	private static final String REDIRECT_URI = "api/oauth/qq/callback";

	public QQOauth() {
		super();
		setClientId(OjConfig.getString("openid_qq"));
		setClientSecret(OjConfig.getString("openkey_qq"));
		setRedirectUri(OjConfig.getBaseURL() + "/" + REDIRECT_URI);
	}

	public String getAuthorizeUrl() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("state", "qq");
		params.put("response_type", "code");
		params.put("client_id", getClientId());
		params.put("redirect_uri", getRedirectUri());
		return super.getAuthorizeUrl(AUTH_URL, params);
	}

	public String getTokenByCode(String code) throws IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", code);
		params.put("client_id", getClientId());
		params.put("client_secret", getClientSecret());
		params.put("grant_type", "authorization_code");
		params.put("redirect_uri", getRedirectUri());
		// access_token=FE04************************CCE2&expires_in=7776000
		String token = TokenUtil.getAccessToken(super.doGet(TOKEN_URL, params));
		log.info(token);
		return token;
	}

	public String getTokenInfo(String accessToken) throws IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		// callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} );
		String openid = TokenUtil.getOpenId(super.doGet(TOKEN_INFO_URL, params));
		log.info(openid);
		return openid;
	}

	public String getUserInfo(String accessToken, String uid) throws IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		params.put("oauth_consumer_key", getClientId());
		params.put("openid", uid);
		params.put("format", "json");
		// // {"ret":0,"msg":"","nickname":"YOUR_NICK_NAME",...}
		String userinfo = super.doGet(USER_INFO_URL, params);
		log.info(userinfo);
		return userinfo;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getUserInfoByCode(String code) throws IOException {
		String accessToken = getTokenByCode(code);
		String openId = getTokenInfo(accessToken);
		Map<String, String> dataMap = JSON.parseObject(getUserInfo(accessToken, openId), Map.class);
		dataMap.put("openid", openId);
		return dataMap;
	}

}
