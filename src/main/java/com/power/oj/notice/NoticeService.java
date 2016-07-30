package com.power.oj.notice;

import com.jfinal.plugin.activerecord.Page;
import com.power.oj.core.OjConfig;
import com.power.oj.user.UserService;
import com.power.oj.util.Tool;

import java.util.List;

public final class NoticeService {
    private static final NoticeModel dao = NoticeModel.dao;
    private static final NoticeService me = new NoticeService();
    private static final UserService userService = UserService.me();

    private NoticeService() {
    }

    public static NoticeService me() {
        return me;
    }

    public NoticeModel getNotice(Integer id) {
        String sql = "SELECT n.*,u.name,FROM_UNIXTIME(startTime, '%Y-%m-%d %H:%i:%s') AS startDateTime,"
            + "FROM_UNIXTIME(endTime, '%Y-%m-%d %H:%i:%s') AS endDateTime FROM notice n "
            + "INNER JOIN user u ON u.uid=n.uid WHERE id=?";
        if (!userService.isAdmin()) {
            sql += " AND startTime<=UNIX_TIMESTAMP() AND n.status=1";
        }

        NoticeModel noticeModel = dao.findFirst(sql, id);
        if (noticeModel == null) {
            return null;
        }
        noticeModel.setView(noticeModel.getView() + 1);

        noticeModel.update();

        return noticeModel;
    }

    public List<NoticeModel> getNoticeList() {
        return dao.find("SELECT id,title FROM notice WHERE "
            + "startTime<=UNIX_TIMESTAMP() AND endTime>=UNIX_TIMESTAMP() AND status=1 ORDER BY id DESC");
    }

    public Page<NoticeModel> getNoticePage(int pageNumber, int pageSize) {
        String sql = "SELECT n.*,u.name";
        String from =
            "FROM notice n INNER JOIN user u ON u.uid=n.uid";
        if (!userService.isAdmin()) {
            from += " WHERE startTime<=UNIX_TIMESTAMP() AND n.status=1";
        }
        from += " ORDER BY id DESC";

        return dao.paginate(pageNumber, pageSize, sql, from);
    }

    public boolean saveNotice(NoticeModel noticeModel) {
        NoticeModel newNotice = new NoticeModel();

        newNotice.setUid(userService.getCurrentUid());
        newNotice.setCid(noticeModel.getCid());
        newNotice.setTitle(noticeModel.getTitle());
        newNotice.setStartTime(noticeModel.getStartTime());
        newNotice.setEndTime(noticeModel.getEndTime());
        newNotice.setContent(noticeModel.getContent());
        newNotice.setCtime(OjConfig.timeStamp);

        return newNotice.save();
    }

    public boolean updateNotice(NoticeModel noticeModel) {
        Integer id = noticeModel.getId();
        NoticeModel newNotice = dao.findById(id);

        newNotice.setEditorUid(userService.getCurrentUid());
        newNotice.setCid(noticeModel.getCid());
        newNotice.setTitle(noticeModel.getTitle());
        newNotice.setStartTime(noticeModel.getStartTime());
        newNotice.setEndTime(noticeModel.getEndTime());
        newNotice.setContent(noticeModel.getContent());
        newNotice.setStatus(Tool.getBoolean(noticeModel.getStatus()));
        newNotice.setMtime(OjConfig.timeStamp);

        return newNotice.update();
    }

}
