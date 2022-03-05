package com.aeo.test.dao;

import com.aeo.test.bean.AdminsBean;
import com.aeo.test.bean.Auth;
import com.aeo.test.bean.Menu;
import com.aeo.test.bean.Role;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper //标记mapper文件位置，否则在Application.class启动类上配置mapper包扫描
@Repository
public interface AdminsDao {

    /**
     * 查询用户名是否存在，若存在，不允许注册
     * 注解@Param(value) 若value与可变参数相同，注解可省略
     * 注解@Results  列名和字段名相同，注解可省略
     * @param username
     * @return
     */

    @Select(value = "select * from admins a where a.username=#{username} and a.password=#{password}")
    @Results
            ({@Result(property = "username",column = "username"),
                    @Result(property = "password",column = "password")})
    AdminsBean findUserByName(@Param("username") String username,@Param("password") String password);

        /**
         * 注册  插入一条admins记录
         * @param adminsBean
         * @return
         */
        @Insert("insert into admins(username,password,addTime) values(#{username},#{password},#{addTime})")
        //option注解标签useGeneratedKeys=true表示使用数据库自动增长的主键，keyColumn用于指定数据库table中的主键，keyProperty用于指定传入对象的成员变量。
        //这个注解的意思就是，使用数据库自动增长的主键，并从table中userId字段里面把数据放到传入对象的成员变量userId里面。
        //如果我们已经在数据库表中指定了主键，那么keyColumn属性可以缺省。
        @Options(useGeneratedKeys = true,keyProperty = "userId")
        void register(AdminsBean adminsBean);

        /**
         * 登录
         * @param adminsBean
         * @return
         */
        @Select("select a.userId from admins a where a.username = #{username} and a.password = #{password}")
        Integer login(AdminsBean adminsBean);

    /**
     * 通过userId来得到roleName
     * @param userId
     * @return
     */
    @Select("select roleName from admins where userId = #{userId}")
    String getRoleNameLogin(@Param("userId") Integer userId);

    /**
     * 找到对应的管理员名字的roleId
     * @param roleName
     * @return
     */
    @Select("select id from role where roleName=#{roleName}")
    Integer getRoleId(@Param("roleName") String roleName);

    /**
     * 关联auth表：注册的时候就插入信息（userId,menuAuth,roleId）
     * @param userId
     * @return
     */
    @Insert("insert into auth(userId,menuAuth,roleId) values(#{userId},#{menuAuth},#{roleId})")
    @Options(useGeneratedKeys = true)
    void withAuthHasRole(@Param("userId") Integer userId,@Param("menuAuth") Integer menuAuth,@Param("roleId") Integer roleId);


    /**
     * 关联auth表：注册的时候就插入信息
     * @param userId
     * @return
     */
    @Insert("insert into auth(userId) values(#{userId})")
    @Options(useGeneratedKeys = true)
    void withAuth(@Param("userId") Integer userId);

    /**
     * 查询auth表
     */
    @Select("select * from auth where userId=#{userId}")
    Auth queryAuth(@Param("userId") Integer userId);

    /**
     * 设置auth表的信息，用于登录后更新token等信息
     */
    @Update("update auth set token=#{token},addTime=#{addTime},ip=#{ip} where userId=#{userId}")
    public void updateAuth(Auth auth);

    /**
     * 超级管理员查询表menu信息，只找父菜单
     */
    @Select("select menuId,authName,childrenId,order1,path from menu where level = 0")
    List<Menu> adminSelectMenus();

    /**
     * 查询表menu信息，只找父菜单
     */
    @Select("select menuId,authName,childrenId,order1,path from menu where menuAuth=#{menuAuth} and level=0")
    List<Menu> selectMenus(@Param("menuAuth") int menuAuth);

    /**
     * 查询表menu信息，只查一条信息
     */
    @Select("select menuId,authName,childrenId,order1,path,menuAuth from menu where menuId = #{menuId}")
    Menu selectMenu(@Param("menuId") int menuId);

    /**
     * 根据token值查询menuAuth是0还是1
     */
    @Select("select menuAuth from auth where token=#{token}")
    Integer getAuth(@Param("token") String token);

    /**
     * 超级管理员查询：返回所有用户信息
     */
    @Select("select * from admins limit #{limit} offset #{offset}")
    List<AdminsBean> getAllUsers(@Param("limit") int limit,@Param("offset") int offset);

    /**
     * 超级管理员查询：当前用户总数
     */
    @Select("select count(*) from admins")
    Integer getUsersNum();

    /**
     * 超级管理员查询：通过username模糊查询，返回所有用户信息
     */
    @Select("select * from admins where username like concat('%',#{query},'%') limit #{limit} offset #{offset}")
    List<AdminsBean> getAllUsersByQuery(@Param("limit") int limit,@Param("offset") int offset,@Param("query")String query);

    /**
     * 超级管理员查询：通过username模糊查询，当前用户总数
     */
    @Select("select count(*) from admins where username like concat('%',#{query},'%')")
    Integer getUsersNumByQuery(@Param("query")String query);

    /**
     * 超级管理员查询：返回所有的角色信息
     */
    @Select("select * from role")
    List<Role> getAllRoles();

    /**
     * 超级管理员操作：修改密码
     */
    @Update("update admins set password=#{password} where userId=#{userId}")
    int changePassword(@Param("password")String password,@Param("userId")int userId);

    /**
     * 查找rid对应的角色名称
     */
    @Select("select roleName,id from role where id=#{rid}")
    @Results
            ({@Result(property = "roleName",column = "roleName"),
                    @Result(property = "id",column = "id")})
    Role getRoleName(@Param("rid")int rid);

    /**
     * 超级管理员操作：修改角色名称
     */
    @Update("update admins set roleName=#{roleName} where userId=#{userId}")
    int changeRoleName(@Param("roleName")String roleName,@Param("userId")int userId);

    /**
     * 超级管理员操作：修改角色menuAuth和roleId
     */
    @Update("update auth set menuAuth=#{menuAuth},roleId=#{roleId} where userId=#{userId}")
    int changeAuthRole(@Param("menuAuth")int menuAuth,@Param("roleId")int roleId,@Param("userId")int userId);

    /**
     * 超级管理员操作：删除用户
     */
    @Delete("delete from admins where userId = #{userId}")
    int deleteUser(@Param("userId")int userId);

    /**
     * 超级管理员操作：删除用户对应的auth
     */
    @Delete("delete from auth where userId = #{userId}")
    int deleteUserFromAuth(@Param("userId")int userId);

    @Select("select username,password from admins where userId = #{userId}")
    @Results
            ({@Result(property = "username",column = "username"),
                    @Result(property = "password",column = "password")})
    AdminsBean findPassword(@Param("userId")int userId);
}
