package com.power.oj.shiro;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.service.OjService;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;

public class OjAuthorizingRealm extends AuthorizingRealm {

	private static final Logger log = Logger.getLogger(OjAuthorizingRealm.class);

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Integer uid = (Integer) principals.fromRealm(getName()).iterator().next();

		if (uid != null) {
			if (OjConfig.isDevMode()) {
				log.info(uid.toString());
			}
			List<Record> roleList = OjService.me().getUserRoles(uid);

			if (roleList != null && roleList.size() > 0) {
				SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
				Set<String> roles = new HashSet<String>();
				Set<String> pers = new HashSet<String>();

				for (Record record : roleList) {
					roles.add(record.getStr("role"));
					if (OjConfig.isDevMode()) {
						log.info("role: " + record.getStr("role"));
					}

					List<Record> permissionList = OjService.me().getRolePermission(record.getInt("rid"));
					for (Record permissionRecord : permissionList) {
						pers.add(permissionRecord.getStr("permission"));
						if (OjConfig.isDevMode()) {
							log.info("permission: " + permissionRecord.getStr("permission"));
						}
					}
				}
				info.setRoles(roles);
				info.setStringPermissions(pers);

				return info;
			}
		}

		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
		UserModel userModel = UserService.me().getUserByName(token.getUsername());

		if (userModel != null) {
			AuthenticationInfo info = new SimpleAuthenticationInfo(userModel.getUid(), userModel.getPassword(), getName());
			clearCachedAuthorizationInfo(info.getPrincipals());
			if (OjConfig.isDevMode()) {
				log.info("clearCachedAuthorizationInfo: " + info.getPrincipals());
			}
			return info;
		}

		return null;
	}

	/**
	 * 更新用户授权信息缓存.
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
				if (OjConfig.isDevMode()) {
					log.info("clearAllCachedAuthorizationInfo:" + cache + " key: " + key);
				}
			}
		}
	}

}
