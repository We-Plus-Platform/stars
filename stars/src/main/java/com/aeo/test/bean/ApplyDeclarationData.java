package com.aeo.test.bean;

import java.util.ArrayList;

//declaration 返回数据实体
public class ApplyDeclarationData extends Data {
    private ArrayList<DeclareInfo> details = new ArrayList<>();

    public ArrayList<DeclareInfo> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<DeclareInfo> details) {
        this.details = details;
    }
}
