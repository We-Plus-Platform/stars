package com.aeo.test.service;

import com.aeo.test.bean.Result;

public interface UsersService {
    //接口users，返回所有用户
    public Result returnUsers(String token,String query,int pagenum,int pagesize);

    //接口roles，返回所有角色
    public Result returnRoles(String token);

    //接口user/{userId}/password 修改密码
    public Result changePassword(String token,int userId,String password);

    //接口user/{userId}/role 修改角色
    //rid为role表里面对应的id
    public Result changeRole(String token,int rid,int userId);

    //接口user/{userId} 删除用户
    public Result deleteUser(String token,int userId);

    public Result findPassword(int userId, String token);
}
