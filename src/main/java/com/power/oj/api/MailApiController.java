package com.power.oj.api;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.plugin.activerecord.Page;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.mail.MailModel;

@Before(CheckGuestInterceptor.class)
public class MailApiController extends OjController {
	public void unRead() {
		Integer uid = userService.getCurrentUid();
		long unReadMail = mailService.countUserNewMails(uid);

		renderJson("{\"success\":true, \"unReadMail\":" + unReadMail + "}");
	}

	public void getGroups() {
		int pageNumber = getParaToInt("page", 1);
		int pageSize = getParaToInt("size", OjConfig.mailGroupPageSize);
		Integer uid = userService.getCurrentUid();

		Page<MailModel> mailList = mailService.getUserMailGroups(pageNumber, pageSize, uid);

		renderJson(mailList);
	}

	public void getMails() {
		int pageNumber = getParaToInt("page", 1);
		int pageSize = getParaToInt("size", OjConfig.mailPageSize);
		String p2p = getPara("p2p");
		Integer uid = userService.getCurrentUid();
		Integer peer = Integer.parseInt(p2p.split("-")[1]);

		Page<MailModel> mailList = mailService.getMails(pageNumber, pageSize, uid, peer);

		renderJson(mailList);
	}

	@ClearInterceptor(ClearLayer.ALL)
	public void isHaveUnreaded() {
		String p2p = getPara("p2p");
		Integer uid = userService.getCurrentUid();
		Integer peer = Integer.parseInt(p2p.split("-")[1]);
		boolean result = mailService.hasNewMails(uid, peer);

		renderJson("{\"success\":true, \"status\":200,\"result\":" + result + "}");
	}

	public void newMail() {
		String username = getPara("username");
		String content = getPara("content");
		Integer from = userService.getCurrentUid();
		Integer to = getParaToInt("userId");

		if (to == null && username != null) {
			to = userService.getUidByName(username);
		}

		if (to == null) {
			renderJson("{\"success\":false, \"status\":-100,\"result\":\"Cannot find user.\"}");
		} else if (from.equals(to)) {
			renderJson("{\"success\":false, \"status\":-200,\"result\":\"Cannot send mail to user self.\"}");
		} else if (mailService.sendMail(from, to, content)) {
			renderJson("{\"success\":true, \"status\":200,\"result\":\"\"}");
		} else {
			renderJson("{\"success\":false, \"status\":500,\"result\":\"Save new mail failed.\"}");
		}
	}

	public void deleteMail() {
		Integer uid = userService.getCurrentUid();
		Integer mailId = getParaToInt("mailId");

		if (mailService.deleteMail(uid, mailId) > 0) {
			renderJson("{\"success\":true, \"status\":200,\"result\":\"\"}");
		} else {
			renderJson("{\"success\":false, \"status\":500,\"result\":\"Delete mail failed.\"}");
		}
	}

	public void deleteGroup() {
		String p2p = getPara("p2p");
		Integer uid = userService.getCurrentUid();
		Integer peer = Integer.parseInt(p2p.split("-")[1]);

		if (mailService.deleteMailGroup(uid, peer) > 0) {
			renderJson("{\"success\":true, \"status\":200,\"result\":\"\"}");
		} else {
			renderJson("{\"success\":false, \"status\":500,\"result\":\"Delete mail group failed.\"}");
		}
	}

	public void delAllMails() {
		Integer uid = userService.getCurrentUid();

		if (mailService.deleteUserAllMails(uid) > 0) {
			renderJson("{\"success\":true, \"status\":200,\"result\":\"\"}");
		} else {
			renderJson("{\"success\":false, \"status\":500,\"result\":\"Delete mails failed.\"}");
		}
	}

	public void newDrift() {
		String content = getPara("content");
		Integer from = userService.getCurrentUid();
		int result = mailService.sendDrift(from, content);

		if (result > 0) {
			renderJson("{\"success\":true, \"status\":200,\"result\":" + result + "}");
		} else {
			renderJson("{\"success\":false, \"status\":-200,\"result\":\"Send drift bottle failed.\"}");
		}
	}

	public void getDrift() {
		Integer uid = userService.getCurrentUid();
		Integer result = mailService.getDrift(uid);

		if (result > 0) {
			renderJson("{\"success\":true, \"status\":200,\"result\":" + result + "}");
		} else if (result == 0) {
			renderJson("{\"success\":false, \"status\":-200,\"result\":\"Cannot find a drift bottle.\"}");
		} else {
			renderJson("{\"success\":false, \"status\":-200,\"result\":\"You get too many drift bottles today.\"}");
		}
	}

	public void newBanlistItem() {
		Integer uid = userService.getCurrentUid();
		Integer userId = getParaToInt("userId");

		if (mailService.addMailBanlistItem(uid, userId)) {
			renderJson("{\"success\":true, \"status\":200,\"result\":\"\"}");
		} else {
			renderJson("{\"success\":false, \"status\":500,\"result\":\"Add mail banlist item failed.\"}");
		}
	}

	public void getBanlist() {
		int pageNumber = getParaToInt("page", 1);
		int pageSize = getParaToInt("size", OjConfig.mailPageSize);
		Integer uid = userService.getCurrentUid();

		Page<MailModel> mailList = mailService.getUserMailBanlist(pageNumber, pageSize, uid);

		renderJson(mailList);
	}

	public void deleteBanlistItem() {
		Integer uid = userService.getCurrentUid();
		Integer userId = getParaToInt("userId");

		if (mailService.deleteMailBanlistItem(uid, userId) > 0) {
			renderJson("{\"success\":true, \"status\":200,\"result\":\"\"}");
		} else {
			renderJson("{\"success\":false, \"status\":500,\"result\":\"Delete mail banlist item failed.\"}");
		}
	}

}
