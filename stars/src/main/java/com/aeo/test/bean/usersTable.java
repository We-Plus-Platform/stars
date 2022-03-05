package com.aeo.test.bean;

//users表实体
public class usersTable {
    private int id;
    private String username;
    private String password;
    private String hasVoted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(String hasVoted) {
        this.hasVoted = hasVoted;
    }
}
