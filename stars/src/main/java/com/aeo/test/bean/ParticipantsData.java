package com.aeo.test.bean;

import java.util.ArrayList;
//participants 返回数据实体
public class ParticipantsData extends Data{
    private ArrayList<ParticipantsList> participantsList = new ArrayList<>();
    private int total;  //总的用户数据

    public ArrayList<ParticipantsList> getParticipantsList() {
        return participantsList;
    }

    public void setParticipantsList(ArrayList<ParticipantsList> participantsList) {
        this.participantsList = participantsList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
