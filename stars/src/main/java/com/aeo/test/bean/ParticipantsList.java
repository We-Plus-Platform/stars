package com.aeo.test.bean;

public class ParticipantsList {
    protected int stuId;
    protected String name;
    protected String stuNum;
    protected String college;
//    protected String star;
    private String theWay;
    protected String status;
    protected String addTime;
    private int times;

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStuNum() {
        return stuNum;
    }

    public void setStuNum(String stuNum) {
        this.stuNum = stuNum;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

//    public String getStar() {
//        return star;
//    }
//
//    public void setStar(String star) {
//        this.star = star;
//    }

    public String getTheWay() {
        return theWay;
    }

    public void setTheWay(String theWay) {
        this.theWay = theWay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
