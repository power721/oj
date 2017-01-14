package com.power.oj.mail;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.user.UserExtModel;
import com.power.oj.util.Tool;

import java.util.List;

public final class MailService {
    private static final MailService me = new MailService();
    private static final MailModel dao = MailModel.dao;

    private MailService() {
    }

    public static MailService me() {
        return me;
    }

    public boolean sendMail(Integer from, Integer to, String content) {
        MailContentModel mailContent = new MailContentModel();

        mailContent.set("fromUid", from);
        mailContent.set("toUid", to);
        mailContent.set("content", content);
        mailContent.set("ctime", OjConfig.timeStamp);
        if (!mailContent.save()) {
            return false;
        }

        Integer mid = mailContent.getInt("id");
        MailModel mail = new MailModel();
        mail.set("mid", mid).set("user", from).set("peer", to);
        mail.save();

        if (!isUserInMailBanlist(from, to)) {
            mail = new MailModel();
            mail.set("mid", mid).set("user", to).set("peer", from);
            mail.save();
        }

        return true;
    }

    public int sendDrift(Integer from, String content) {
        UserExtModel userExtModel = UserExtModel.dao.findById(from);
        int timestamp = Tool.getDayTimestamp();
        int drift = userExtModel.getSendDriftNum();
        int last_drift = userExtModel.getLastSendDrift();

        if (last_drift + OjConstants.DAY_TIMESTAMP < timestamp)
            drift = 0;

        if (drift < 10) {
            MailContentModel mailContent = new MailContentModel();

            mailContent.set("fromUid", from);
            mailContent.set("toUid", 0);
            mailContent.set("content", content);
            mailContent.set("ctime", OjConfig.timeStamp);
            mailContent.save();

            drift += 1;
            userExtModel.setSendDriftNum(drift).setLastSendDrift(OjConfig.timeStamp).update();
        }

        return drift;
    }

    public int getDrift(Integer uid) {
        UserExtModel userExtModel = UserExtModel.dao.findById(uid);
        int timestamp = Tool.getDayTimestamp();
        int drift = userExtModel.getGetDriftNum();
        int lastDrift = userExtModel.getLastGetDrift();

        if (lastDrift + OjConstants.DAY_TIMESTAMP < timestamp)
            drift = 0;

        if (drift < 5) {
            MailContentModel mailContent = MailContentModel.dao.getRandMail(uid);

            if (mailContent != null) {
                MailModel mail = new MailModel();
                mail.set("mid", mailContent.get("id")).set("user", uid).set("peer", mailContent.get("fromUid"));
                mail.save();

                mailContent.set("toUid", uid).update();
                drift += 1;
                userExtModel.setGetDriftNum(drift).setLastGetDrift(OjConfig.timeStamp).update();
                return drift;
            }
            return 0;
        }

        return -1;
    }

    public List<MailModel> findUserNewMails(Integer uid) {
        return dao.findUserNewMails(uid);
    }

    public Long countUserNewMails(Integer uid) {
        return dao.countUserNewMails(uid);
    }

    public Long countUserNewMails(Integer user, Integer peer) {
        return dao.countUserNewMails(user, peer);
    }

    public boolean hasNewMails(Integer user, Integer peer) {
        return dao.hasNewMails(user, peer);
    }

    public void resetUserNewMails(Integer user, Integer peer) {
        dao.resetUserNewMails(user, peer);
    }

    public Page<MailModel> getUserMailGroups(int pageNumber, int pageSize, Integer uid) {
        String sql = "SELECT m.*,mc.content,mc.ctime,u.name AS peeruser,u.avatar AS avatar";
        String from =
            "FROM (SELECT * FROM mail WHERE user=? AND status!=2 ORDER BY id DESC)m LEFT JOIN mail_content mc ON mc.id=m.mid LEFT JOIN user u ON u.uid=m.peer GROUP BY peer ORDER BY m.id DESC";

        return dao.paginate(pageNumber, pageSize, sql, from, uid);
    }

    public Page<MailModel> getMails(int pageNumber, int pageSize, Integer user, Integer peer) {
        resetUserNewMails(user, peer);

        String sql = "SELECT m.id,mc.fromUid,mc.toUid,mc.content,mc.ctime,u1.name AS fromuser,u2.name AS touser";
        String from =
            "FROM mail m LEFT JOIN mail_content mc ON mc.id=m.mid LEFT JOIN user u1 ON u1.uid=mc.fromUid LEFT JOIN user u2 ON u2.uid=mc.toUid WHERE user=? AND peer=? AND m.status!=2 ORDER BY id DESC";

        return dao.paginate(pageNumber, pageSize, sql, from, user, peer);
    }

    public int deleteMail(Integer uid, Integer id) {
        return dao.deleteMail(uid, id);
    }

    public int deleteMailGroup(Integer user, Integer peer) {
        return dao.deleteMailGroup(user, peer);
    }

    public int deleteUserAllMails(Integer uid) {
        return dao.deleteUserAllMails(uid);
    }

    public boolean addMailBanlistItem(Integer user, Integer banUser) {
        if (isUserInMailBanlist(banUser, user)) {
            return true;
        }

        Record record = new Record();
        record.set("uid", user);
        record.set("bannedUid", banUser);
        record.set("ctime", OjConfig.timeStamp);

        return Db.save("mail_banlist", record);
    }

    public int deleteMailBanlistItem(Integer user, Integer banUser) {
        return Db.update("DELETE FROM mail_banlist WHERE uid=? AND bannedUid=?", user, banUser);
    }

    public boolean isUserInMailBanlist(Integer banUser, Integer user) {
        return Db.queryInt("SELECT id FROM mail_banlist WHERE uid=? AND bannedUid=?", user, banUser) != null;
    }

    public Page<MailModel> getUserMailBanlist(int pageNumber, int pageSize, Integer user) {
        String sql = "SELECT m.bannedUid AS uid,u.name AS uname";
        String from = "FROM mail_banlist m LEFT JOIN user u ON u.uid=m.bannedUid WHERE m.uid=?";

        return dao.paginate(pageNumber, pageSize, sql, from, user);
    }

}
