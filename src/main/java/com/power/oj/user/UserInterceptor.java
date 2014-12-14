package com.power.oj.user;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.power.oj.core.OjConstants;
import com.power.oj.shiro.ShiroKit;

/**
 * Get and Set current user to request attribute.
 * 
 * @author power
 * 
 */
public class UserInterceptor implements Interceptor {

	@Override
	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();

		if (ShiroKit.isUser()) // if user is logined, set user information in
								// controller
		{
			UserModel userModel = UserService.me().getCurrentUser();
			controller.setAttr(OjConstants.USER, userModel);

			if (UserService.me().isAdmin()) {
				controller.setAttr(OjConstants.ADMIN_USER, userModel.getUid());
			}
		}

		ai.invoke();
	}

}
