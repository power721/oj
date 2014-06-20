package com.power.oj.core.interceptor;

import java.util.Locale;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Const;
import com.jfinal.core.Controller;
import com.jfinal.render.Render;
import com.jfinal.render.RenderFactory;
import com.power.oj.core.AppConfig;

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

    // //////////////////////////////////////////////////////////////////////////////
    if ((Locale.CHINESE.toString()).equals(controller.getCookie(Const.I18N_LOCALE)))
    {
      Render render = controller.getRender();
      if (render == null)
      {
        render = RenderFactory.me().getDefaultRender(ai.getMethodName());
      }
      String view = render.getView();
      if (view == null)
        return;

      if (view.charAt(0) != '/')
      {
        view = ai.getViewPath() + view;
      }
      view = view.replaceFirst(AppConfig.getBaseViewPath(), AppConfig.getBaseViewPath() + "/zh");

      render.setView(view);
      controller.render(render);
    }
    // //////////////////////////////////////////////////////////////////////////////
  }

}
