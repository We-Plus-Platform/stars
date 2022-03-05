package com.aeo.test.bean;

import java.util.ArrayList;

///application/view 返回数据实体
public class ViewData extends Data{
    private ArrayList<ViewParticipants> participantsList = new ArrayList<>();
    private int total;

    public ArrayList<ViewParticipants> getParticipantsList() {
        return participantsList;
    }

    public void setParticipantsList(ArrayList<ViewParticipants> participantsList) {
        this.participantsList = participantsList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
