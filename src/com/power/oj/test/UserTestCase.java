package com.power.oj.test;

import org.junit.Test;

import com.jfinal.ext.test.ControllerTestCase;
import com.power.oj.core.AppConfig;

public class UserTestCase extends ControllerTestCase<AppConfig>
{
  
  @Test
  public void index()
  {
    String url = "/user";
    String resp = use(url).invoke();
    System.out.println(resp);
  }
  
  @Test
  public void rank()
  {
    String url = "/rank";
    String resp = use(url).invoke();
    System.out.println(resp);
  }
  
  @Test
  public void profile()
  {
    String url = "/user/profile";
    String resp = use(url).invoke();
    System.out.println(resp);
  }
  
  @Test
  public void profile4CurrentUser()
  {
    use("/user/signin").post("name=test&password=123456&rememberMe=1").invoke();
    
    String url = "/user/profile";
    String resp = use(url).invoke();
    System.out.println(resp);
  }

  @Test
  public void profile4UserTest()
  {
    String url = "/user/profile/test";
    String resp = use(url).invoke();
    System.out.println(resp);
  }
  
  @Test
  public void infoByUid()
  {
    String url = "/user/info?uid=1000";
    String resp = use(url).invoke();
    System.out.println(resp);
  }

  @Test
  public void infoByName()
  {
    String url = "/user/info?name=test";
    String resp = use(url).invoke();
    System.out.println(resp);
  }

  @Test
  public void infoError()
  {
    String url = "/user/info?uid=test";
    String resp = use(url).invoke();
    System.out.println(resp);
  }
  
  @Test
  public void search()
  {
    String url = "/user/search";
    String body = "word=swust";
    String resp = use(url).post(body).invoke();
    
    System.out.println(resp);
  }
  
  @Test
  public void login()
  {
    String url = "/login";
    String resp = use(url).invoke();
    System.out.println(resp);
  }
  
  @Test
  public void signin()
  {
    String url = "/user/signin";
    String body = "name=test&password=123456&rememberMe=1";
    String resp = use(url).post(body).invoke();
    
    System.out.println(resp);
  }
  
}
