package com.power.oj.core.interceptor;

import java.util.Locale;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

public class I18NInterceptor implements Interceptor
{

  @Override
  public void intercept(ActionInvocation ai)
  {
    Controller controller = ai.getController();
    String lang = controller.getPara("lang");
    Locale locale = null;
    
    if ("zh".equals(lang))
      locale = Locale.CHINESE;
    else if (lang != null)
      locale = Locale.ENGLISH;
    
    if (locale != null)
      controller.setLocaleToCookie(locale);
    
    ai.invoke();
  }

}
