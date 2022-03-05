package com.aeo.test.bean;

//登录返回数据实体
public class LoginData extends MenuInfoData {
    private User user = new User();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
