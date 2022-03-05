package com.aeo.test.controller;


import com.aeo.test.bean.AdminsBean;
import com.aeo.test.bean.Result;
import com.aeo.test.service.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class LoginController {

    //将Service注入Web层
    @Autowired
    LoginService loginService;


    @PutMapping("/application/admin/user")
    public Result register(@RequestBody AdminsBean adminsBean, @RequestHeader("Authorization") String token){
        token = token.replaceAll(" ", "\\+");
        return loginService.register(adminsBean,token);
    }

    @RequestMapping(value = "/application/admin/login",method = {RequestMethod.POST})  //或者PostMapping(value = "/login")
    public Result login(@RequestBody Map<String,Object> map, HttpServletRequest request){  //传入实体
        return loginService.loginIn(map.get("username").toString(),map.get("password").toString(),request);
    }

    @RequestMapping(value="/application/admin/menulist",method = {RequestMethod.GET})
    public Result menuInfo(@RequestHeader("Authorization") String token){    //token在请求头里
        token = token.replaceAll(" ", "\\+");
        return loginService.returnMenuInfo(token);
    }
}
