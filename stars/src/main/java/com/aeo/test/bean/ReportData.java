package com.aeo.test.bean;

import java.util.ArrayList;

//report 返回数据实体
public class ReportData extends Data {
    private ArrayList<Integer> reportData = new ArrayList<>();

    public ArrayList<Integer> getReportData() {
        return reportData;
    }

    public void setReportData(ArrayList<Integer> reportData) {
        this.reportData = reportData;
    }
}
