package com.power.oj.shiro;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.user.UserService;

public class ShiroInViewInterceptor implements Interceptor {

	@Override
	public void intercept(ActionInvocation ai) {
		OjController controller = (OjController) ai.getController();

		if (ShiroKit.isUser()) // if user is logined, set user information in
								// controller
		{
			Integer uid = UserService.me().getCurrentUid();

			if (UserService.me().isAdmin())
				controller.setAttr(OjConstants.ADMIN_USER, uid);
		}

		ai.invoke();
	}

}
