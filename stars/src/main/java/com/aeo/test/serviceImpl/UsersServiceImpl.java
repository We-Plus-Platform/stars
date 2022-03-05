package com.aeo.test.serviceImpl;

import com.aeo.test.bean.*;
import com.aeo.test.dao.AdminsDao;
import com.aeo.test.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(rollbackFor = RuntimeException.class)
@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    private AdminsDao adminsDao;

    /**
     * @param token
     * @param query
     * @param pagenum
     * @param pagesize
     * @return
     */
    @Override
    public Result returnUsers(String token, String query, int pagenum, int pagesize) {
        Result result = new Result();
        UsersData usersData = new UsersData();
        Integer menuAuth;
        ArrayList<UserList> userList = new ArrayList<>();
        List<AdminsBean> adminsBeanList;
        Integer total;
        try {
            menuAuth = adminsDao.getAuth(token);
            if (menuAuth == null || menuAuth.equals(1)) {  //token无效或越权查询
                usersData.getMeta().setStatus("400");
                usersData.getMeta().setMsg("token无效或越权查询");
                result.setData(usersData);
                return result;
            }
            if(query == null || query.equals("")) {
                adminsBeanList = adminsDao.getAllUsers(pagesize, (pagenum - 1) * pagesize);
                total = adminsDao.getUsersNum();
            }else{
                adminsBeanList = adminsDao.getAllUsersByQuery(pagesize, (pagenum - 1) * pagesize,query);
                total = adminsDao.getUsersNumByQuery(query);
            }
            if (!adminsBeanList.isEmpty()) {
                for (AdminsBean adminsBean : adminsBeanList) {
                    UserList userList1 = new UserList();
                    userList1.setUserId(adminsBean.getUserId());
                    userList1.setUsername(adminsBean.getUsername());
                    userList1.setRoleName(adminsBean.getRoleName());
                    userList1.setAddTime(adminsBean.getAddTime());
                    userList.add(userList1);
                }
                usersData.setUserList(userList);
            } else {
                usersData.getMeta().setStatus("400");
                usersData.getMeta().setMsg("无更多用户");
                result.setData(usersData);
                return result;
            }
            usersData.setTotal(total);
        } catch (Exception e) {
            e.printStackTrace();
        }
        usersData.getMeta().setStatus("200");
        usersData.getMeta().setMsg("查询成功");
        result.setData(usersData);
        return result;
    }

    @Override
    public Result returnRoles(String token) {
        Result result = new Result();
        Integer menuAuth;
        RolesData rolesData = new RolesData();
        List<Role> roles;
        try {
            menuAuth = adminsDao.getAuth(token);
            if (menuAuth == null || menuAuth.equals(1)) {      //token无效或越权查询
                rolesData.getMeta().setStatus("400");
                rolesData.getMeta().setMsg("token无效或越权查询");
                result.setData(rolesData);
                return result;
            }
            roles = adminsDao.getAllRoles();
            ArrayList<Role> rolesList = new ArrayList<>(roles);
            rolesData.setRolesList(rolesList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        rolesData.getMeta().setStatus("200");
        rolesData.getMeta().setMsg("查询成功");
        result.setData(rolesData);
        return result;
    }

    @Override
    public Result changePassword(String token,int userId, String password) {
        Result result = new Result();
        Integer menuAuth;
        Data data = new Data();
        try {
            menuAuth = adminsDao.getAuth(token);
            if (menuAuth == null || menuAuth.equals(1)) {      //token无效或越权查询
                data.getMeta().setStatus("400");
                data.getMeta().setMsg("token无效或越权查询");
                result.setData(data);
                return result;
            }
            int res = adminsDao.changePassword(password,userId);
            if(res == 0){
                data.getMeta().setStatus("400");
                data.getMeta().setMsg("修改失败");
                result.setData(data);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        data.getMeta().setStatus("200");
        data.getMeta().setMsg("修改成功");
        result.setData(data);
        return result;
    }

    @Override
    public Result changeRole(String token,int rid,int userId) {
        Result result = new Result();
        Integer menuAuth;
        Data data = new Data();
        try{
            menuAuth = adminsDao.getAuth(token);
            if (menuAuth == null || menuAuth.equals(1)) {      //token无效或越权查询
                data.getMeta().setStatus("400");
                data.getMeta().setMsg("token无效或越权查询");
                result.setData(data);
                return result;
            }
            Role role = adminsDao.getRoleName(rid);
            int res = adminsDao.changeRoleName(role.getRoleName(),userId);
            if(res == 0){
                data.getMeta().setStatus("400");
                data.getMeta().setMsg("修改失败");
                result.setData(data);
                return result;
            }
            int menuAuth2;
            if(role.getId() == 0){
                menuAuth2 = 0;
            }else{
                menuAuth2 = 1;   //普通管理员
            }
            int res2 = adminsDao.changeAuthRole(menuAuth2,role.getId(),userId);
//            if(res2 == 0){
//                data.getMeta().setStatus("400");
//                data.getMeta().setMsg("修改失败");
//                result.setData(data);
//                return result;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        data.getMeta().setStatus("200");
        data.getMeta().setMsg("修改成功");
        result.setData(data);
        return result;
    }

    @Override
    public Result deleteUser(String token, int userId) {
        Result result = new Result();
        Integer menuAuth;
        Data data = new Data();
        try {
            menuAuth = adminsDao.getAuth(token);
            if (menuAuth == null || menuAuth.equals(1)) {      //token无效或越权查询
                data.getMeta().setStatus("400");
                data.getMeta().setMsg("token无效或越权查询");
                result.setData(data);
                return result;
            }
            int res = adminsDao.deleteUser(userId);
            if(res == 0){
                data.getMeta().setStatus("400");
                data.getMeta().setMsg("删除失败");
                result.setData(data);
                return result;
            }
            int res2 = adminsDao.deleteUserFromAuth(userId);
            if(res2 == 0){
                data.getMeta().setStatus("400");
                data.getMeta().setMsg("删除失败");
                result.setData(data);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        data.getMeta().setStatus("200");
        data.getMeta().setMsg("删除成功");
        result.setData(data);
        return result;
    }

    @Override
    public Result findPassword(int userId, String token) {
        Result result = new Result();
        FindPassData findPassData = new FindPassData();
        Integer menuAuth;
        AdminsBean adminsBean;
        try{
            menuAuth = adminsDao.getAuth(token);
            if (menuAuth == null || menuAuth.equals(1)) {      //token无效或越权查询
                findPassData.getMeta().setStatus("400");
                findPassData.getMeta().setMsg("token无效或越权查询");
                result.setData(findPassData);
                return result;
            }
            adminsBean = adminsDao.findPassword(userId);
            if(adminsBean == null){
                findPassData.getMeta().setStatus("400");
                findPassData.getMeta().setMsg("未找到该用户信息");
                result.setData(findPassData);
                return result;
            }
            findPassData.setUsername(adminsBean.getUsername());
            findPassData.setPassword(adminsBean.getPassword());
            findPassData.getMeta().setStatus("200");
            findPassData.getMeta().setMsg("查询密码成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData(findPassData);
        return result;
    }
}
