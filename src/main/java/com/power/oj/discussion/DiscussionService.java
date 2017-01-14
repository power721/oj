package com.power.oj.discussion;

import com.jfinal.plugin.activerecord.Page;
import com.power.oj.core.OjConfig;
import com.power.oj.user.UserService;
import jodd.util.HtmlEncoder;

import java.util.ArrayList;
import java.util.List;

public final class DiscussionService {
    private static final TopicModel dao = TopicModel.dao;
    private static final DiscussionService me = new DiscussionService();
    private static final UserService userService = UserService.me();

    private DiscussionService() {
    }

    public static DiscussionService me() {
        return me;
    }

    public Page<TopicModel> getTopicPage(int pageNumber, int pageSize, Integer pid) {
        List<Object> paras = new ArrayList<Object>();
        StringBuilder sb = new StringBuilder();
        sb.append("FROM `topic` t INNER JOIN `user` u ON u.uid=t.uid WHERE 1=1");
        if (pid != null && pid > 0) {
            sb.append(" AND pid=?");
            paras.add(pid);
        }
        sb.append(" AND parentId=0 ORDER BY orderNum DESC");

        Page<TopicModel> topicPage =
            dao.paginate(pageNumber, pageSize, "SELECT t.*,u.name", sb.toString(), paras.toArray());

        return topicPage;
    }

    public List<TopicModel> getTopicList(Page<TopicModel> topicPage) {
        List<TopicModel> topicList = new ArrayList<TopicModel>();

        for (TopicModel topic : topicPage.getList()) {
            topicList.addAll(getTopicTree(topic));
        }
        return topicList;
    }

    public List<TopicModel> getTopicTree(TopicModel topic) {
        List<TopicModel> topicList = new ArrayList<TopicModel>();

        topicList.add(topic);
        List<TopicModel> children = dao.find(
            "SELECT t.*,u.name FROM `topic` t INNER JOIN `user` u ON u.uid=t.uid WHERE t.parentId=? ORDER BY t.orderNum DESC,t.id",
            topic.getId());
        for (TopicModel child : children) {
            topicList.addAll(getTopicTree(child));
        }

        return topicList;
    }

    public TopicModel findTopic(Integer id) {
        return dao.findFirst("SELECT * FROM `topic` WHERE id=?", id);
    }

    public TopicModel findTopic4Show(Integer id) {
        TopicModel topic = dao.findFirst(
            "SELECT t.*,p.title AS problem,u.name FROM `topic` t LEFT JOIN problem p ON p.pid=t.pid "
                + "INNER JOIN user u ON u.uid=t.uid WHERE t.id=?", id);
        // topic.setView(topic.getView() + 1).update();
        return topic;
    }

    public boolean addDiscussion(TopicModel topicModel) {
        topicModel.setUid(userService.getCurrentUid());
        topicModel.setCtime(OjConfig.timeStamp);
        topicModel.setTitle(HtmlEncoder.text(topicModel.getTitle()));
        topicModel.setContent(topicModel.getContent());

        if (topicModel.save()) {
            Integer id = topicModel.getId();
            Integer threadId = topicModel.getThreadId();
            Integer parentId = topicModel.getParentId();

            while (parentId != null && parentId != 0) {
                TopicModel parent = dao.findById(parentId);
                parent.setOrderNum(id);
                parentId = parent.getParentId();
                parent.update();
            }

            topicModel.setOrderNum(id);
            if (threadId == null || threadId == 0) {
                topicModel.setThreadId(id);
            }
            return topicModel.update();
        }
        return false;
    }

    public boolean updateDiscussion(TopicModel topicModel) {
        TopicModel newTopic = dao.findById(topicModel.getId());

        newTopic.setTitle(HtmlEncoder.text(topicModel.getTitle()));
        newTopic.setContent(topicModel.getContent());
        newTopic.setMtime(OjConfig.timeStamp);

        return newTopic.update();
    }

    public boolean removeDiscussion(TopicModel topicModel) {
        topicModel.setStatus(false);
        return topicModel.update();
    }

    public boolean deleteDiscussion(TopicModel topicModel) {
        return topicModel.delete();
    }
}
