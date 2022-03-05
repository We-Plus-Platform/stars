package com.aeo.test.bean;


import com.alibaba.fastjson.JSONObject;

//file 的返回数据实体
public class FileData extends Data{
    private JSONObject credentials;

    public JSONObject getCredentials() {
        return credentials;
    }

    public void setCredentials(JSONObject credentials) {
        this.credentials = credentials;
    }
}
