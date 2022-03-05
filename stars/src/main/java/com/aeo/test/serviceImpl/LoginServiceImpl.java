package com.aeo.test.serviceImpl;

import com.aeo.test.bean.*;
import com.aeo.tools.CurrentTime;
import com.aeo.test.dao.AdminsDao;
import com.aeo.test.service.LoginService;
import com.aeo.tools.IpUtil;
import com.aeo.tools.TokenProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Transactional(rollbackFor = RuntimeException.class)
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AdminsDao adminsDao;


    /**
     * 注册
     *
     * @param adminsBean 参数封装
     * @return Result
     */
    @Override
    public Result register(AdminsBean adminsBean,String token) {
        Result result = new Result();
        Data data = new Data();
        try {
            Integer menuAuth = adminsDao.getAuth(token);
            if(menuAuth == null || !menuAuth.equals(0)){
                data.getMeta().setMsg("token无效或者越权操作");
                data.getMeta().setStatus("400");
                result.setData(data);
                return result;
            }
            AdminsBean existAdmin = adminsDao.findUserByName(adminsBean.getUsername(),adminsBean.getPassword());
            if(existAdmin != null){
                //如果用户名已存在
                data.getMeta().setMsg("用户已存在");
                data.getMeta().setStatus("400");
                result.setData(data);
                return result;
            }
//        插入时间addTime
            adminsBean.setAddTime(CurrentTime.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
                adminsDao.register(adminsBean);
            data.getMeta().setMsg("添加用户成功");
            data.getMeta().setStatus("200");
            result.setData(data);
        } catch (Exception e) {
            data.getMeta().setMsg(e.getMessage());
            result.setData(data);
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 登录
     *
     * @param username,password 用户名和密码
     * @return Result
     */
    @Override
    public Result loginIn(String username, String password, HttpServletRequest request) {
        AdminsBean adminsBean = new AdminsBean();
        adminsBean.setUsername(username);
        adminsBean.setPassword(password);
//        System.out.println(adminsBean);
        Result result = new Result();
        LoginData data = new LoginData();
        data.getMeta().setStatus("400");
        Auth auth;
        try {
            Integer userId = adminsDao.login(adminsBean);
            if (userId == null) {
                data.getMeta().setStatus("400");
                data.getMeta().setMsg("用户名或密码错误");

            } else {
                //查一下auth表
                auth = adminsDao.queryAuth(userId);
                if(auth == null){    //如果没有auth信息，就关联一下
                    //看这个userId对应的账户有没有分配角色
                    String roleName = adminsDao.getRoleNameLogin(userId);
                    if(roleName != null){  //已经分配过角色了
                        Integer roleId = adminsDao.getRoleId(roleName);
                        Integer menuAuth = null;
                        if(roleId.equals(0)){
                            menuAuth = 0;
                        }else{
                            menuAuth = 1;
                        }
                        adminsDao.withAuthHasRole(userId,menuAuth,roleId);
                    }
                    else{
                        adminsDao.withAuth(userId);
                    }
                    auth = adminsDao.queryAuth(userId);
                }
                //id
                int id = adminsBean.getUserId();
                data.getUser().setId(id);
                //name
                data.getUser().setName(adminsBean.getRoleName());
                //token
                TokenProcessor tokenProcessor = TokenProcessor.getInstance();
                String token = tokenProcessor.makeToken();
                data.getUser().setToken(token);
                auth.setToken(token);   //更新token值
                auth.setAddTime(CurrentTime.getCurrentTime("yyyy-MM-dd HH:mm:ss"));   //更新token 的创建时间
                //ip
                auth.setIp(IpUtil.getIpAddr(request));   //更新ip
                //更新auth表
                adminsDao.updateAuth(auth);
//                //menuList
//                List<Menu> menus;
//                if(auth.getMenuAuth() == 0){   //是超管
//                    menus = adminsDao.adminSelectMenus();
//                }else{   //普通管理员
//                    menus = adminsDao.selectMenus(auth.getMenuAuth());
//                }
//                ArrayList<MenuList> menulist = new ArrayList<>(menus.size());
//                for(Menu menu:menus){
//                    MenuList menuList = new MenuList();
//                    menuList.setPath(menu.getPath());
//                    menuList.setOrder(menu.getOrder1());
//                    menuList.setAuthName(menu.getAuthName());
//                    menuList.setId(menu.getMenuId());  //menuId对应前端的id
//                    if(menu.getChildrenId() != null && !menu.getChildrenId().equals("")){
//                        String[] ss = menu.getChildrenId().split(",");
//                        for(String string:ss){
//                            //注意：此处子菜单不能再有子菜单，否则算法有问题
//                            Menu child = adminsDao.selectMenu(Integer.parseInt(string));
//                            //装进childList
//                            MenuList childList = new MenuList();
//                            childList.setId(child.getMenuId());
//                            childList.setOrder(child.getOrder1());
//                            childList.setPath(child.getPath());
//                            menuList.getChildren().add(childList);  //添加子菜单
//                        }
//                    }
//                    menulist.add(menuList);
//                }
//                data.setMenulist(menulist);
                //meta
                data.getMeta().setStatus("200");
                data.getMeta().setMsg("登录成功");


            }
        } catch (Exception e) {
            e.printStackTrace();
            data.getMeta().setStatus("400");
            data.getMeta().setMsg(e.getMessage());
        }
        result.setData(data);
        return result;
    }

    /**
     * 返回菜单信息
     *
     * @param token 权限信息
     * @return Result
     */
    @Override
    public Result returnMenuInfo(String token) {
        MenuInfoData menuInfoData = new MenuInfoData();
        List<Menu> menus;
        Integer menuAuth;
        Result result = new Result();
        try {
            menuAuth = adminsDao.getAuth(token);
            if(menuAuth == null){
                menuInfoData.getMeta().setStatus("400");
                menuInfoData.getMeta().setMsg("token未找到");
                result.setData(menuInfoData);
                return result;
            }
            if (menuAuth == 0) {  //超级管理员
                menus = adminsDao.adminSelectMenus();
            } else {
                menus = adminsDao.selectMenus(menuAuth);
            }
            ArrayList<MenuList> menulist = new ArrayList<>(menus.size());
            for (Menu menu : menus) {
                MenuList menuList = new MenuList();
                menuList.setPath(menu.getPath());
                menuList.setOrder(menu.getOrder1());
                menuList.setAuthName(menu.getAuthName());
                menuList.setId(menu.getMenuId());  //menuId对应前端的id
                if (menu.getChildrenId() != null && !menu.getChildrenId().equals("")) {
                    String[] ss = menu.getChildrenId().split(",");
                    for (String string : ss) {
                        //注意：此处子菜单不能再有子菜单，否则算法有问题
                        Menu child = adminsDao.selectMenu(Integer.parseInt(string));
                        if(child.getMenuAuth() == 0 && menuAuth.equals(1)){
                            continue;
                        }
                        //装进childList
                        MenuList childList = new MenuList();
                        childList.setAuthName(child.getAuthName());
                        childList.setId(child.getMenuId());
                        childList.setOrder(child.getOrder1());
                        childList.setPath(child.getPath());
                        menuList.getChildren().add(childList);  //添加子菜单
                    }
                }
                menulist.add(menuList);
            }
            menuInfoData.setMenulist(menulist);
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }
        menuInfoData.getMeta().setStatus("200");
        menuInfoData.getMeta().setMsg("查询成功");
        result.setData(menuInfoData);
        return result;
    }

}
