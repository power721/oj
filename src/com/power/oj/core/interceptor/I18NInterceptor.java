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
    
    if ("en".equals(lang))
      locale = Locale.ENGLISH;
    else if ("zh".equals(lang))
      locale = Locale.CHINESE;
    
    if (locale != null)
    controller.setLocaleToCookie(locale);
    
    ai.invoke();
  }

}
