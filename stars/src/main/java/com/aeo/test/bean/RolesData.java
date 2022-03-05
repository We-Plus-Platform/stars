package com.aeo.test.bean;


import java.util.ArrayList;

//roles 返回数据实体
public class RolesData extends Data{
    private ArrayList<Role> rolesList = new ArrayList<>();

    public ArrayList<Role> getRolesList() {
        return rolesList;
    }

    public void setRolesList(ArrayList<Role> rolesList) {
        this.rolesList = rolesList;
    }
}
