package com.power.oj.core.interceptor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpSession;

import jodd.util.StringBand;
import jodd.util.StringUtil;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.power.oj.core.OjConstants;

/**
 * Update session table with uri and timestamp.
 * 
 * @author power
 * 
 */
public class AccessLogInterceptor implements Interceptor
{
	protected final Logger log = Logger.getLogger(getClass());
	private String skipActions[] =
	{ "/login", "/logout", "/captcha" };

	@Override
	public void intercept(ActionInvocation ai)
	{
		Controller controller = ai.getController();
		HttpSession session = controller.getSession(true);
		String actionKey = ai.getActionKey();

		if (StringUtil.equalsOne(actionKey, skipActions) == -1)
		{
			StringBand rsb = new StringBand(actionKey);
			if (controller.getPara() != null)
				rsb.append("/").append(controller.getPara());

			StringBand sb = new StringBand(controller.getRequest().getRequestURI());
			String query = controller.getRequest().getQueryString();
			if (query != null)
			{
				sb.append("?").append(query);
				rsb.append("?").append(query);
			}

			try
			{
				controller.setAttr(OjConstants.REDIRECT_URI, URLEncoder.encode(rsb.toString(), "UTF-8"));
				controller.setAttr("uri", URLEncoder.encode(sb.toString(), "UTF-8"));
			} catch (UnsupportedEncodingException e)
			{
				log.error(e.getLocalizedMessage());
			}

			Db.update("UPDATE session SET last_activity=UNIX_TIMESTAMP(),uri=? WHERE session_id=?", sb.toString(), session.getId());
		}

		ai.invoke();
	}

}
