package com.aeo.test.bean;

import java.util.ArrayList;

//users 返回数据实体
public class UsersData extends Data {
    private ArrayList<UserList> userList = new ArrayList<>();
    private int total;  //总的用户数据

    public ArrayList<UserList> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<UserList> userList) {
        this.userList = userList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
