package com.aeo.test.dao;

import com.aeo.test.bean.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Result;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ApplyDao {

    /**
     * 学生登录
     * @param username
     * @param password
     * @return
     */
    @Select("select id from students where username=#{username} and password=#{password}")
    Integer login(String username, String password);

    /**
     * 查询ip地址黑名单，返回主键
     * @param ip
     * @return
     */
    @Select("select id,`active` from ip_black where ip=#{ip} and name=#{name} and stuNum=#{stuNum}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "active", column = "active")
    })
    IpBlack queryBlackIp(@Param("ip")String ip,@Param("name")String name,@Param("stuNum")String stuNum);

    /**
     * 添加黑名单ip
     * @param ip
     */
    @Insert("insert into ip_black(ip,name,stuNum) values(#{ip},#{name},#{stuNum})")
    void insertIpBlack(@Param("ip")String ip,@Param("name")String name,@Param("stuNum")String stuNum);

    /**
     * 查询ip登录账号的次数
     * @param ip
     * @return
     */
    @Select("select count(*) from stuauth where ip=#{ip}")
    Integer getIpTimes(@Param("ip")String ip);

    /**
     * 关联stuAuth表
     * @param stuId
     */
    @Insert("insert into stuauth(stuId) values(#{stuId})")
    @Options(useGeneratedKeys = true)
    void withAuth(@Param("stuId")int stuId);

    /**
     * 查询stuAuth表
     * @param stuId
     * @return
     */
    @Select("select * from stuauth where stuId=#{stuId}")
    StuAuthTable queryAuth(@Param("stuId") Integer stuId);


    /**
     * 更新权限信息
     * @param auth
     */
    @Update("update stuauth set token=#{token},addTime=#{addTime},ip=#{ip} where stuId=#{stuId}")
    void updateAuth(StuAuthTable auth);

    /**
     * 得到学生id：stuId
     * @param token
     * @return
     */
    @Select("select stuId from stuauth where token=#{token}")
    Integer selectUserId(@Param("token")String token);

    /**
     * 通过stuId获得学生的学号
     * @param stuId
     * @return
     */
    @Select("select stuNum from students where id = #{stuId}")
    String selectStuNum(@Param("stuId")int stuId);

    /**
     * check 姓名和学号，返回数据
     * @param username
     * @param stuNum
     * @return
     */
    @Select("select * from students where username=#{username} and stuNum=#{stuNum}")
    StudentsTable check(@Param("username")String username,@Param("stuNum")String stuNum);

    /**
     * check的时候无误就插入这些信息
     * @param participants
     */
//    @Insert("insert into participants(stuNum,name,major,theWay,addTime,grade,sex,birthday,nation,college,collegeId,classNum,star,phonenum,assistant) values(#{stuNum},#{name},#{major},#{theWay},#{addTime},#{grade},#{sex},#{birthday},#{nation},#{college},#{collegeId},#{classNum},#{star},#{phonenum},#{assistant})")
    @Insert("insert into participants(stuNum,name,major,theWay,addTime,grade,sex,birthday,nation,college,collegeId,classNum,phonenum,assistant) values(#{stuNum},#{name},#{major},#{theWay},#{addTime},#{grade},#{sex},#{birthday},#{nation},#{college},#{collegeId},#{classNum},#{phonenum},#{assistant})")
    @Options(useGeneratedKeys = true,keyProperty = "stuId",keyColumn = "stuId")
    void insertParticipant(Participants participants);

    /**
     * 通过学号查询创建时间，可能有多个，选择最晚的一个用户
     * @param stuNum
     * @return
     */
    @Select("select addTime from participants where stuNum=#{stuNum}")
    List<String> selectAddTime(@Param("stuNum")String stuNum);
    /**
     * 通过最后一次创建时间来查participants的stuId
     * @param addTime
     * @return
     */
    @Select("select stuId from participants where addTime=#{addTime}")
    Integer selectParStuId(@Param("addTime")String addTime);

    /**
     * 通过学号来更新participants的信息
     * @param star
     * @param phonenum
     * @param assistant
     * @param stuNum
     */
//    @Update("update participants set star=#{star},phonenum=#{phonenum},assistant=#{assistant} where stuNum=#{stuNum}")
    @Update("update participants set phonenum=#{phonenum},assistant=#{assistant} where stuNum=#{stuNum}")
    void updateParticipant(@Param("phonenum")String phonenum,@Param("assistant")String assistant,@Param("stuNum")String stuNum);

    /**
     * 插入申报材料信息
     * @param declaration
     */
    @Insert("insert into declaration values(#{stuId},#{rank},#{honor},#{paper},#{file},#{person},#{patent},#{techprogram},#{reason})")
    void insertDeclaration(Declaration declaration);

    /**
     * 查看所有已推荐的同学
     * @return
     */
    @Select("select * from participants where status='1' order by vote desc,stuId limit #{limit} offset #{offset}")
    List<Participants> viewParticipants(@Param("limit") int limit, @Param("offset") int offset);

    /**
     * 已推荐同学的总数
     * @return
     */
    @Select("select count(*) from participants where status='1'")
    Integer viewParticipantsNum();

    /**
     * 通过query来查看所有已推荐的同学
     * @return
     */
    @Select("select * from participants where status='1' and (stuId like concat('%',#{query},'%') or name like concat('%',#{query},'%')) order by vote desc,stuId limit #{limit} offset #{offset}")
    List<Participants> viewParticipantsByQuery(@Param("limit") int limit, @Param("offset") int offset,@Param("query") String query);

    /**
     * 通过query来查看已推荐同学的总数
     * @return
     */
    @Select("select count(*) from participants where status='1' and (stuId like concat('%',#{query},'%') or name like concat('%',#{query},'%'))")
    Integer viewParticipantsNumByQuery(@Param("query") String query);

    /**
     * 由stuAuth表的stuId来查students表的id对应的voteNum和lastVoteTime
     * @param stuId
     * @return
     */
    @Select("select voteNum,lastVoteTime from students where id=#{stuId}")
    @Results
            ({@Result(property = "voteNum",column = "voteNum"),
                    @Result(property = "lastVoteTime",column = "lastVoteTime")})
    Vote selectVoteInfo(@Param("stuId")int stuId);

    /**
     * 通过stuId（对应students的id）来更新投票状态信息
     * @param lastVoteTime
     * @param voteNum
     * @param stuId
     */
    @Update("update students set lastVoteTime=#{lastVoteTime},voteNum=#{voteNum} where id=#{stuId}")
    void updateVoteInfo(@Param("lastVoteTime")String lastVoteTime,@Param("voteNum")int voteNum,@Param("stuId")int stuId);

    /**
     * 投票
     * @param stuId
     */
    @Update("update participants set vote = vote+1 where stuId=#{stuId}")
    void vote(@Param("stuId") int stuId);

    /**
     * 通过名字来查“已经推荐”的同学信息
     * @param name
     * @return
     */
    @Select("select * from participants where status='1' and (name = #{name} or stuNum = #{name})")
    List<Participants> viewName(@Param("name")String name);

    @Select("select file from declaration where stuId=#{stuId}")
    String getFilePath(@Param("stuId")int stuId);

//    /**
//     * 通过名字来查“已经推荐”的同学，获取总数
//     * @param name
//     * @return
//     */
//    @Select("select count(*) from participants where status='1' and name = #{name}")
//    Integer viewNameNum(@Param("name")String name);

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

}
