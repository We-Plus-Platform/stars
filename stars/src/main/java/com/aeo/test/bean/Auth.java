package com.aeo.test.bean;

//auth表实体
public class Auth {
    private int id;
    private int userId;
    private String token;
    private String addTime;
    private String ip;
    private int roleId;
    private int menuAuth;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getMenuAuth() {
        return menuAuth;
    }

    public void setMenuAuth(int menuAuth) {
        this.menuAuth = menuAuth;
    }
}
