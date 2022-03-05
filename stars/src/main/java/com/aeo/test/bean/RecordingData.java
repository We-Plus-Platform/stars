package com.aeo.test.bean;

import java.util.ArrayList;

//recording 返回数据实体
public class RecordingData extends Data {
    private ArrayList<RecordList> recordList = new ArrayList<>();
    private int total;

    public ArrayList<RecordList> getRecordList() {
        return recordList;
    }

    public void setRecordList(ArrayList<RecordList> recordList) {
        this.recordList = recordList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
