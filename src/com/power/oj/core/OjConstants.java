package com.power.oj.core;

/**
 * Some constant objects.
 * 
 * @author power
 * 
 */
public interface OjConstants
{
  /*
   * These contants are used in general.
   */
  long SessionExpiresTime = 15 * 60 * 1000;
  long resetPasswordExpiresTime = 30 * 60 * 1000;
  int avatarWidth = 100;
  int avatarHeight = 100;
  
  /*
   * These contants are used in view(attr name) and session.
   */
  String SITE_TITLE = "siteTitle";
  String PAGE_TITLE = "pageTitle";
  String BASE_URL = "baseUrl";
  
  String CONTROLLER_KEY = "controllerKey";
  String ACTION_KEY = "actionKey";
  String METHOD_NAME = "methodName";

  String MSG = "msg";
  String MSG_TYPE = "msgType";
  String MSG_TITLE = "msgTitle";

  String USER = "user";
  String USER_ID = "userID";
  String USER_NAME = "userName";
  String USER_EMAIL = "userEmail";
  String USER_LIST = "userList";
  String ADMIN_USER = "adminUser";

  String PROGRAM_LANGUAGES = "program_languages";
  String LANGUAGE_NAME = "language_name";
  String JUDGE_RESULT = "judge_result";
  String RESULT_TYPE = "result_type";
  
  String LAST_ACCESS_URL = "lastAccessURL";
  
  /*
   * These contants are used in cookie.
   */
  String randomCodeKey = "PowerOJCaptcha";
  
  int TOKEN_AGE = 3600 * 24 * 7;
  String TOKEN_NAME = "PowerOjName";
  String TOKEN_TOKEN = "PowerOjToken";

}
