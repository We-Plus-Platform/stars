package com.aeo.test.controller;

import com.aeo.test.bean.PasswordInfo;
import com.aeo.test.bean.Result;
import com.aeo.test.bean.RoleInfo;
import com.aeo.test.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersController {
    //将Service注入Web层
    @Autowired
    UsersService usersService;

    @RequestMapping(value = "/application/admin/user",method = {RequestMethod.GET})
    public Result returnUsers(@RequestHeader("Authorization")String token, @RequestParam("query") String query,
                              @RequestParam("pagenum") int pagenum, @RequestParam("pagesize") int pagesize){  //传入实体
        token = token.replaceAll(" ", "\\+");
        return usersService.returnUsers(token,query,pagenum,pagesize);
    }

    @RequestMapping(value="/application/admin/roles",method = {RequestMethod.GET})
    public Result returnRoles(@RequestHeader("Authorization")String token){
        token = token.replaceAll(" ","\\+");
        return usersService.returnRoles(token);
    }

    @RequestMapping(value="/application/admin/user/{userId}/password",method={RequestMethod.PUT})
    public Result changePassword(@RequestHeader("Authorization")String token, @RequestBody PasswordInfo passwordInfo){
        token = token.replaceAll(" ","\\+");
        return usersService.changePassword(token,passwordInfo.getUserId(),passwordInfo.getPassword());
    }

    @RequestMapping(value="/application/admin/user/{userId}/role",method = {RequestMethod.PUT})
    public Result changeRole(@RequestBody RoleInfo roleInfo,@RequestHeader("Authorization")String token,@PathVariable("userId")String userIdStr){
        token = token.replaceAll(" ","\\+");
        int userId = Integer.parseInt(userIdStr);
        return usersService.changeRole(token,roleInfo.getRid(),userId);
    }

    @DeleteMapping(value = "/application/admin/user/{userId}")
    public Result deleteUser(@PathVariable("userId")String userIdStr,@RequestHeader("Authorization")String token){
        token = token.replaceAll(" ","\\+");
        int userId = Integer.parseInt(userIdStr);
        return usersService.deleteUser(token,userId);
    }

    @GetMapping("/application/admin/user/{userId}/password")
    public Result findPassword(@PathVariable("userId")String userIdStr,@RequestHeader("Authorization")String token){
        token = token.replaceAll(" ","\\+");
        int userId = Integer.parseInt(userIdStr);
        return usersService.findPassword(userId,token);
    }

}
