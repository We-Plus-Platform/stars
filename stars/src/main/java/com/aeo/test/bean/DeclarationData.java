package com.aeo.test.bean;

import java.util.ArrayList;

//declaration 返回数据实体
public class DeclarationData extends Data {
    private ArrayList<DeclareInfo> declarationInfo = new ArrayList<>();

    public ArrayList<DeclareInfo> getDeclarationInfo() {
        return declarationInfo;
    }

    public void setDeclarationInfo(ArrayList<DeclareInfo> declarationInfo) {
        this.declarationInfo = declarationInfo;
    }
}
