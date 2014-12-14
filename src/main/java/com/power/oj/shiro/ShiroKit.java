package com.power.oj.shiro;

import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

public class ShiroKit {
	private static final String NAMES_DELIMETER = ",";

	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	public static boolean hasRole(String roleName) {
		return getSubject() != null && roleName != null && roleName.length() > 0 && getSubject().hasRole(roleName);
	}

	public static boolean lacksRole(String roleName) {
		return !hasRole(roleName);
	}

	public static boolean hasAnyRoles(String roleNames) {
		boolean hasAnyRole = false;
		Subject subject = getSubject();
		if (subject != null && roleNames != null && roleNames.length() > 0) {
			for (String role : roleNames.split(NAMES_DELIMETER)) {
				if (subject.hasRole(role.trim())) {
					hasAnyRole = true;
					break;
				}
			}
		}
		return hasAnyRole;
	}

	public static boolean hasAllRoles(String roleNames) {
		boolean hasAllRole = true;
		Subject subject = getSubject();
		if (subject != null && roleNames != null && roleNames.length() > 0) {
			for (String role : roleNames.split(NAMES_DELIMETER)) {
				if (!subject.hasRole(role.trim())) {
					hasAllRole = false;
					break;
				}
			}
		}
		return hasAllRole;
	}

	public static boolean hasPermission(String permission) {
		return getSubject() != null && permission != null && permission.length() > 0 && getSubject().isPermitted(permission);
	}

	public static boolean lacksPermission(String permission) {
		return !hasPermission(permission);
	}

	public static boolean isAuthenticated() {
		return getSubject() != null && getSubject().isAuthenticated();
	}

	public static boolean isRemembered() {
		return getSubject() != null && getSubject().isRemembered();
	}

	public static boolean isUser() {
		return getSubject() != null && getSubject().getPrincipal() != null;
	}

	public static boolean isGuest() {
		return !isUser();
	}

	public static String getPrincipal() {
		if (getSubject() != null) {
			Object principal = getSubject().getPrincipal();
			if (principal != null)
				return principal.toString();
		}
		return "Guest";
	}

	public static Session getCurrentSession() {
		return SecurityUtils.getSubject().getSession();
	}

	public static String getCurrentSessionId() {
		return (String) SecurityUtils.getSubject().getSession().getId();
	}

	public static DefaultWebSecurityManager getSecurityManager() {
		return (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
	}

	public static DefaultWebSessionManager getSessionManager() {
		return (DefaultWebSessionManager) getSecurityManager().getSessionManager();
	}

	public static Collection<Session> getActiveSessions() {
		return getSessionManager().getSessionDAO().getActiveSessions();
	}
}
