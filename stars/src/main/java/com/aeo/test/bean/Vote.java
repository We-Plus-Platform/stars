package com.aeo.test.bean;

//查询数据库的voteNum和lastVoteTime的结果实体
public class Vote {
    private int voteNum;
    private String lastVoteTime;

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

    public String getLastVoteTime() {
        return lastVoteTime;
    }

    public void setLastVoteTime(String lastVoteTime) {
        this.lastVoteTime = lastVoteTime;
    }
}
