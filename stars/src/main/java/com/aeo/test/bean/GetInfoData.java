package com.aeo.test.bean;

import java.util.ArrayList;

//declaration 返回数据实体
public class GetInfoData extends Data {
    private ArrayList<DeclarationInfo> declarationInfo = new ArrayList<>();

    public ArrayList<DeclarationInfo> getDeclarationInfo() {
        return declarationInfo;
    }

    public void setDeclarationInfo(ArrayList<DeclarationInfo> declarationInfo) {
        this.declarationInfo = declarationInfo;
    }
}
