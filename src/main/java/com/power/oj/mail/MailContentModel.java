package com.power.oj.mail;

import com.jfinal.plugin.activerecord.Model;

public class MailContentModel extends Model<MailContentModel> {
    public static final MailContentModel dao = new MailContentModel();
    public static final String ID = "id";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String CONTENT = "content";
    public static final String CTIME = "ctime";
    /**
     *
     */
    private static final long serialVersionUID = 3375076623779739301L;

    public MailContentModel getRandMail(Integer uid) {
        String sql =
            "SELECT t1.* FROM `mail_content` AS t1 JOIN (SELECT ROUND(RAND() * ((SELECT MAX(id) FROM `mail_content`)-(SELECT MIN(id) FROM `mail_content`))+(SELECT MIN(id) FROM `mail_content`)) AS id) AS t2 WHERE t1.id >= t2.id AND `toUid`=0 AND `fromUid`!=? ORDER BY t1.id LIMIT 1";
        return dao.findFirst(sql, uid);
    }

}
