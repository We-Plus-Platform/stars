package com.aeo.test.dao;

import com.aeo.test.bean.Declaration;
import com.aeo.test.bean.DeclarationInfo;
import com.aeo.test.bean.Participants;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AwardsDao {

    /**
     * 查询token对应的roleId
     */

    @Select("select roleId from auth where token=#{token}")
    Integer getRoleId(@Param("token") String token);

    /**
     * 查询token对应的userId
     */

    @Select("select userId from auth where token=#{token}")
    Integer getUserId(@Param("token") String token);

    /**
     * 查询userId对应的username
     */

    @Select("select username from admins where userId=#{userId}")
    String getUsername(@Param("userId") int userId);

    /**
     * 超级管理员查询：返回所有的参与者
     */
    @Select("select * from participants limit #{limit} offset #{offset}")
    List<Participants> getAllParticipants(@Param("limit") int limit, @Param("offset") int offset);

    /**
     * 超级管理员查询：返回所有的参与者的数量
     */
    @Select("select count(*) from participants")
    Integer getAllParticipantsNum();

    /**
     * 超级管理员查询：返回所有的参与者，根据query模糊查询
     */
    @Select("select * from participants where stuId like concat('%',#{query},'%') or name like concat('%',#{query},'%') limit #{limit} offset #{offset}")
    List<Participants> getAllParticipantsByQuery(@Param("query") String query,@Param("limit") int limit, @Param("offset") int offset);

    /**
     * 超级管理员查询：返回所有的参与者的数量
     */
    @Select("select count(*) from participants where stuId like concat('%',#{query},'%') or name like concat('%',#{query},'%')")
    Integer getAllParticipantsNumByQuery(@Param("query") String query);

    /**
     * 超级管理员去重查询
     * @param query
     * @param limit
     * @param offset
     * @return
     */
    @Select("select * from participants as a" +
            " where not exists (" +
            " select 1 from participants as b" +
            " where b.name=a.name and b.stuId>a.stuId)" +
            " and (stuId like concat('%',#{query},'%') " +
            " or name like concat('%',#{query},'%'))" +
            " limit #{limit} offset #{offset}")
    List<Participants> getAllDistinctParticipants(@Param("query") String query,@Param("limit") int limit, @Param("offset") int offset);

    /**
     * 超级管理员去重查询数量
     * @param query
     * @return
     */
    @Select("select count(*) from participants as a" +
            " where not exists (" +
            " select 1 from participants as b" +
            " where b.name=a.name and b.stuId>a.stuId)" +
            " and (stuId like concat('%',#{query},'%') " +
            " or name like concat('%',#{query},'%'))")
    Integer getAllDistinctParticipantsNum(@Param("query") String query);


    /**
     * 通过roleId来查询对应的参与者
     */
    @Select("select * from participants where collegeId=#{collegeId} limit #{limit} offset #{offset}")
    List<Participants> getParticipants(@Param("collegeId") int collegeId, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * 普通管理员查询：返回对应的参与者的数量
     */
    @Select("select count(*) from participants where collegeId=#{collegeId}")
    Integer getParticipantsNum(@Param("collegeId")int collegeId);

    /**
     * 通过roleId来查询对应的参与者
     */
    @Select("select * from participants where collegeId=#{collegeId} and (stuId like concat('%',#{query},'%') or name like concat('%',#{query},'%')) limit #{limit} offset #{offset}")
    List<Participants> getParticipantsByQuery(@Param("collegeId") int collegeId,@Param("query") String query, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * 普通管理员查询：返回对应的参与者的数量
     */
    @Select("select count(*) from participants where collegeId=#{collegeId} and (stuId like concat('%',#{query},'%') or name like concat('%',#{query},'%'))")
    Integer getParticipantsNumByQuery(@Param("collegeId")int collegeId,@Param("query") String query);

    /**
     * 普通管理员去重查询
     * @param collegeId
     * @param query
     * @param limit
     * @param offset
     * @return
     */
    @Select("select * from participants as a" +
            " where not exists (" +
            " select 1 from participants as b" +
            " where b.name=a.name and b.stuId>a.stuId)" +
            " and collegeId=#{collegeId}"+
            " and (stuId like concat('%',#{query},'%') " +
            " or name like concat('%',#{query},'%'))" +
            " limit #{limit} offset #{offset}")
    List<Participants> getDistinctParticipants(@Param("collegeId") int collegeId,@Param("query") String query,@Param("limit") int limit, @Param("offset") int offset);

    /**
     * 普通管理员去重查询数量
     * @param query
     * @return
     */
    @Select("select count(*) from participants as a" +
            " where not exists (" +
            " select 1 from participants as b" +
            " where b.name=a.name and b.stuId>a.stuId)" +
            " and collegeId=#{collegeId}"+
            " and (stuId like concat('%',#{query},'%') " +
            " or name like concat('%',#{query},'%'))")
    Integer getDistinctParticipantsNum(@Param("collegeId") int collegeId,@Param("query") String query);

    /**
     * 查询学生信息
     */
    @Select("select * from participants where stuId=#{stuId}")
    Participants getParticipantsInfo(@Param("stuId")int stuId);

    /**
     * 查询申报材料
     */
    @Select("select * from declaration where stuId=#{stuId}")
    Declaration getDeclaration(@Param("stuId")int stuId);

    /**
     * 判断管理员是否有权限审核参与者
     */
    @Select("select collegeId from participants where stuId=#{stuId}")
    Integer getCollegeId(@Param("stuId")int stuId);

    /**
     * 根据roleId得到管理员的角色名称
     */
    @Select("select roleName from role where id=#{roleId}")
    String getRoleName(@Param("roleId")int roleId);

    /**
     * 审核结果
     */
    @Update("update participants set status=#{newStatus},handler=#{handler},verifyTime=#{verifyTime} where stuId=#{stuId}")
    Integer updateStatus(@Param("newStatus")int newStatus,@Param("handler")String handler,@Param("stuId")int stuId,@Param("verifyTime")String verifyTime);

    /**
     * 查询审核结果，由管理员的名字来查询
     */
    @Select("select * from participants where handler <> \"\" limit #{limit} offset #{offset}")
    List<Participants> getRecording( @Param("limit") int limit, @Param("offset") int offset);

    /**
     * 查询审核结果，由管理员的名字来查询审核后的参与者数量
     */
    @Select("select count(*) from participants where handler <> \"\"")
    Integer getTotalRecording();

    /**
     * 查询审核结果，由管理员的名字来查询，根据query模糊查询
     */
    @Select("select * from participants where handler <> \"\" and (stuId like concat('%',#{query},'%') or name like concat('%',#{query},'%')) limit #{limit} offset #{offset}")
    List<Participants> getRecordingByQuery(@Param("query") String query, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * 查询审核结果，由管理员的名字来查询审核后的参与者数量，根据query模糊查询
     */
    @Select("select count(*) from participants where handler <> \"\" and (stuId like concat('%',#{query},'%') or name like concat('%',#{query},'%'))")
    Integer getTotalRecordingByQuery(@Param("query") String query);

    /**
     * 超级管理员查询：处于不同状态的同学人数
     */
    @Select("select count(*) from participants where status = #{status}")
    Integer getStatusNum(@Param("status")int status);

    /**
     * 普通管理员查询：处于不同状态的同学人数
     */
    @Select("select count(*) from participants where status = #{status} and collegeId=#{collegeId}")
    Integer getStatusNumNormal(@Param("status")int status,@Param("collegeId")int collegeId);


    /**
     * 申报次数
     * @param stuNum
     * @return
     */
    @Select("select count(*) from participants where stuNum=#{stuNum}")
    Integer getTimes(@Param("stuNum")String stuNum);

    /**
     * 通过stuId来查询学号
     * @param stuId
     * @return
     */
    @Select("select stuNum from participants where stuId=#{stuId}")
    String getStuNum(@Param("stuId")int stuId);

    /**
     * 查询这个学号的同学是否已经推荐过了
     * @param stuNum
     * @return
     */
    @Select("select count(*) from participants where stuNum=#{stuNum} and status='1'")
    Integer verifyTime(@Param("stuNum")String stuNum);

    /**
     * 修改participants表的信息
     * @param declarationInfo
     * @return
     */
//    @Update("update participants set name=#{name},stuNum=#{stuNum},star=#{star},phonenum=#{phonenum},assistant=#{assistant} where stuId=#{stuId}")
    @Update("update participants set name=#{name},stuNum=#{stuNum},phonenum=#{phonenum},assistant=#{assistant} where stuId=#{stuId}")
    Integer changeParInfo(DeclarationInfo declarationInfo);

    /**
     * 修改declaration表的信息
     * @param declarationInfo
     * @return
     */
    @Update("update declaration set `rank`=#{rank},honor=#{honor},paper=#{paper},file=#{file},person=#{person},patent=#{patent},techprogram=#{techprogram},reason=#{reason} where stuId=#{stuId}")
    Integer changeDeclareInfo(DeclarationInfo declarationInfo);
}
