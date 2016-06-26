package com.power.oj.judge;

public class RejudgeTask {
    private Integer cid;
    private Integer id;
    private RejudgeType type;
    private int total = 1;
    private int count = 0;

    public RejudgeTask(Integer id, RejudgeType type) {
        this.id = id;
        this.type = type;
    }

    public RejudgeTask(Integer cid, Integer pid, RejudgeType type) {
        this.cid = cid;
        this.id = pid;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public RejudgeType getType() {
        return type;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void increaseCount() {
        count++;
    }

    public String getKey() {
        if (cid != null) {
            return type.getKey(cid, id);
        }
        return type.getKey(id);
    }

}
