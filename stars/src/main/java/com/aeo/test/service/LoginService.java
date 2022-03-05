package com.aeo.test.service;


import com.aeo.test.bean.AdminsBean;
import com.aeo.test.bean.Result;

import javax.servlet.http.HttpServletRequest;


public interface LoginService {
    public Result register(AdminsBean adminsBean,String token);

    public Result loginIn(String username, String password, HttpServletRequest request);

    public Result returnMenuInfo(String token);

}
