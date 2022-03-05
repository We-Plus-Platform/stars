package com.aeo.test.serviceImpl;

import com.aeo.test.bean.*;
import com.aeo.test.dao.ApplyDao;
import com.aeo.test.service.ApplyService;
import com.aeo.tools.CurrentTime;
import com.aeo.tools.IpUtil;
import com.aeo.tools.TokenProcessor;
import com.tencent.cloud.CosStsClient;

//import com.alibaba.fastjson.JSONObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Transactional(rollbackFor = RuntimeException.class)
@Service
public class ApplyServiceImpl implements ApplyService {
    @Autowired
    ApplyDao applyDao;

    @Override
    public Result login(String username, String password, HttpServletRequest request) {
        Result result = new Result();
        ApplyLoginData applyLoginData = new ApplyLoginData();
        StuAuthTable auth;
        String ip;
        try {
            Integer id = applyDao.login(username, password);
            if (id == null) {
                applyLoginData.getMeta().setStatus("400");
                applyLoginData.getMeta().setMsg("用户名或密码错误");
            } else {
                ip = IpUtil.getIpAddr(request);
                IpBlack ipBlack = applyDao.queryBlackIp(ip, username, password);
                if (ipBlack == null) {
                    Integer ipTimes = applyDao.getIpTimes(ip);
                    if (ipTimes > 1000) {
                        applyDao.insertIpBlack(ip, username, password);  //更新黑名单
                        applyLoginData.getMeta().setStatus("400");
                        applyLoginData.getMeta().setMsg("该用户已登录6个以上账号");
                        result.setData(applyLoginData);
                        return result;
                    }
                }else if (ipBlack.getActive() == 0) {   //在封禁状态
                    applyLoginData.getMeta().setStatus("400");
                    applyLoginData.getMeta().setMsg("该用户已登录6个以上账号");
                    result.setData(applyLoginData);
                    return result;
                }
                //查一下auth表
                auth = applyDao.queryAuth(id);
                if (auth == null) {    //如果没有auth信息，关联一下
                    applyDao.withAuth(id);
                    auth = applyDao.queryAuth(id);
                }
                //token
                TokenProcessor tokenProcessor = TokenProcessor.getInstance();
                String token = tokenProcessor.makeToken();
                applyLoginData.setToken(token);
                auth.setToken(token);   //更新token值
                auth.setAddTime(CurrentTime.getCurrentTime("yyyy-MM-dd HH:mm:ss"));   //更新token 的创建时间
                //ip
                auth.setIp(ip);   //更新ip
                //更新auth表
                applyDao.updateAuth(auth);
                //meta
                applyLoginData.getMeta().setStatus("200");
                applyLoginData.getMeta().setMsg("登录成功");
                System.out.println("loginSuccess:" + username + password + CurrentTime.getCurrentTime(" yyyy-MM-dd HH:mm:ss"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData(applyLoginData);
        return result;
    }

    /**
     * 根据学号和姓名来检查信息是否正确，先插入记录性别等记录
     *
     * @param name
     * @param stuNum
     * @param token
     * @return
     */
    @Override
    public Result check(String name, String stuNum, String token) {
        Result result = new Result();
        Data data = new Data();
        Integer stuId;
        try {
            stuId = applyDao.selectUserId(token);
            if (stuId == null) {
                data.getMeta().setStatus("400");
                data.getMeta().setMsg("token无效");
                result.setData(data);
                return result;
            }
            StudentsTable studentsTable = applyDao.check(name, stuNum);
            if (studentsTable == null) {
                data.getMeta().setStatus("400");
                data.getMeta().setMsg("推荐信息错误");
                result.setData(data);
                return result;
            }
            data.getMeta().setStatus("200");
            data.getMeta().setMsg("信息正确");
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData(data);
        return result;
    }

    @Override
    public Result file(String token) {
        Result result = new Result();
        TreeMap<String, Object> config = new TreeMap<String, Object>();
        FileData fileData = new FileData();
        Integer stuId;
        try {
            stuId = applyDao.selectUserId(token);
            if (stuId == null) {
                fileData.getMeta().setStatus("400");
                fileData.getMeta().setMsg("token无效");
                result.setData(fileData);
                return result;
            }
            // 云 API 密钥 secretId
            config.put("secretId", "AKIDsXzSgkc8Qq2AJBtAICsJVS8AvQecsBm8");
            // 云 API 密钥 secretKey
            config.put("secretKey", "FEH5dAQQBgwxiCUXM0NUK2TlocjjUEXd");
            //若需要设置网络代理，则可以如下设置
//            if (properties.containsKey("https.proxyHost")) {
//                System.setProperty("https.proxyHost", properties.getProperty("https.proxyHost"));
//                System.setProperty("https.proxyPort", properties.getProperty("https.proxyPort"));
//            }

            // 临时密钥有效时长，单位是秒
            config.put("durationSeconds", 1800);

            // 换成你的 bucket
            config.put("bucket", "cqupt-estation-1305209768");
            // 换成 bucket 所在地区
            config.put("region", "ap-chongqing");

            // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的具体路径，
            // 例子： a.jpg 或者 a/* 或者 * (使用通配符*存在重大安全风险, 请谨慎评估使用)
            config.put("allowPrefix", "exampleobject");

            // 密钥的权限列表。简单上传和分片需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
            String[] allowActions = new String[]{
                    // 简单上传
                    "name/cos:PutObject",
                    // 表单上传
                    "name/cos:PostObject",
                    // 分片上传： 初始化分片
                    "name/cos:InitiateMultipartUpload",
                    // 分片上传： 查询 bucket 中未完成分片上传的UploadId
                    "name/cos:ListMultipartUploads",
                    // 分片上传： 查询已上传的分片
                    "name/cos:ListParts",
                    // 分片上传： 上传分片块
                    "name/cos:UploadPart",
                    // 分片上传： 完成分片上传
                    "name/cos:CompleteMultipartUpload"
            };
            config.put("allowActions", allowActions);
            // 请求临时密钥信息
            JSONObject credentialOri = CosStsClient.getCredential(config);
            com.alibaba.fastjson.JSONObject credential = com.alibaba.fastjson.JSONObject.parseObject(credentialOri.toString());
            // 请求成功：打印对应的临时密钥信息
            fileData.setCredentials(credential);
            fileData.getMeta().setStatus("200");
            fileData.getMeta().setMsg("请求成功");
        } catch (Exception e) {
            // 请求失败，抛出异常
//            throw new IllegalArgumentException("no valid secret !");
            e.printStackTrace();
            fileData.getMeta().setStatus("400");
            fileData.getMeta().setMsg(e.getMessage());
        }
        result.setData(fileData);
        return result;
    }

//    /**
//     * 返回临时秘钥
//     *
//     * @param token
//     * @param multipartFile
//     * @return
//     */
//    @Override
//    public Result file(String token, MultipartFile multipartFile) {
//        Result result = new Result();
//        FileData fileData = new FileData();
//        Integer stuId;
//        OutputStream out = null;
//        if (multipartFile.isEmpty()) {
//            fileData.getMeta().setStatus("400");
//            fileData.getMeta().setMsg("表单为空");
//            result.setData(fileData);
//            return result;
//        }
//        try {
//            stuId = applyDao.selectUserId(token);
//            if (stuId == null) {
//                fileData.getMeta().setStatus("400");
//                fileData.getMeta().setMsg("token无效");
//                result.setData(fileData);
//                return result;
//            }
//            String fileName = String.format("%1$tY-%1$tm-%1$td_%1$tH-%1$tM-%1$tS_%1$tL", new Date()) + ".jpg";
//            //     /usr/local/tomcat/webapps/ROOT/StarsFiles/     |D:\IDEA\project\stars\src\main\files\
//            String path = "D:\\IDEA\\project\\stars\\src\\main\\files\\ " + fileName;
//
//            byte[] bytes = multipartFile.getBytes();
//            out = new BufferedOutputStream(new FileOutputStream(
//                    new File(path)));
//            out.write(bytes);
//
//
//            out.flush();
//            out.close();
//            fileData.getMeta().setStatus("200");
//            fileData.getMeta().setMsg("上传成功");
//            fileData.setTmp_path("http://www.estationaeolus.xyz/StarsFiles/" + fileName);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        result.setData(fileData);
//        return result;
//    }

    /**
     * 推荐
     *
     * @param recommend
     * @param token
     * @return
     */
    @Override
    public Result recommend(Recommend recommend, String token) {
        Result result = new Result();
        Data data = new Data();
        Declaration declaration = new Declaration();
        Integer stuId;
        try {
            stuId = applyDao.selectUserId(token);
            if (stuId == null || recommend.getStuNum().equals("")) {
                data.getMeta().setStatus("400");
                data.getMeta().setMsg("token无效或者学号为空");
                result.setData(data);
                return result;
            }
            Participants participants = new Participants();
            String stuNum2 = applyDao.selectStuNum(stuId);
            if (recommend.getStuNum().equals(stuNum2)) {
                participants.setTheWay("自己推荐");
            } else {
                participants.setTheWay("同学推荐");
            }
            participants.setAddTime(CurrentTime.getCurrentTime("yyyy-MM-dd HH:mm:ss"));   //"yyyy-MM-dd HH:mm:ss"
            StudentsTable studentsTable = applyDao.check(recommend.getName(), recommend.getStuNum());
            if (studentsTable == null) {
                data.getMeta().setStatus("400");
                data.getMeta().setMsg("推荐信息错误");
                result.setData(data);
                return result;
            }
            //学院应该是从爬出来的数据来取
            participants.setCollege(studentsTable.getCollege());
            //学院id  collegeId
            HashMap<Integer, String> colleges = new HashMap<>();
            colleges.put(1, "通信与信息工程学院");
            colleges.put(2, "计算机科学与技术学院");
            colleges.put(3, "经济管理学院");
            colleges.put(4, "理学院");
            colleges.put(5, "自动化学院");
            colleges.put(6, "传媒艺术学院");
            colleges.put(7, "先进制造工程学院");
            colleges.put(8, "网络空间安全与信息法学院");
            colleges.put(9, "生物信息学院");
            colleges.put(10, "外国语学院");
            colleges.put(11, "体育学院");
            colleges.put(12, "软件工程学院");
            colleges.put(13, "光电工程学院/国际半导体学院");
            colleges.put(14, "国际学院");
            colleges.put(15, "现代邮政学院");
            for (Map.Entry<Integer, String> entry : colleges.entrySet()) {
                if (studentsTable.getCollege().equals(entry.getValue())) {
                    participants.setCollegeId(entry.getKey());
                    break;
                }
            }
            participants.setName(studentsTable.getUsername());
            participants.setStuNum(studentsTable.getStuNum());
            participants.setMajor(studentsTable.getMajor());
            String classNum = studentsTable.getClassNum();
            if (classNum.length() == 7) {
                classNum = "0" + classNum;
            }
            participants.setClassNum(classNum);
            participants.setGrade(studentsTable.getGrade());
            participants.setSex(studentsTable.getSex());
            participants.setBirthday(studentsTable.getBirthday());
            participants.setNation(studentsTable.getNation());
//            participants.setStar(recommend.getStar());
            participants.setphonenum(recommend.getphonenum());
            participants.setAssistant(recommend.getAssistant());
            applyDao.insertParticipant(participants);
            declaration.setStuId(participants.getStuId());
//            declaration.setGoodDeeds(recommend.getGoodDeeds());
            declaration.setReason(recommend.getReason());
            declaration.setHonor(recommend.getHonor());
//            declaration.setMotto(recommend.getMotto());
//            declaration.setOthers(recommend.getOthers());
            declaration.setTechprogram(recommend.getTechprogram());
            declaration.setPaper(recommend.getPaper());
            declaration.setRank(recommend.getRank());
//            declaration.setRankTest(recommend.getRankTest());
//            declaration.setTechHonor(recommend.getTechHonor());
//            declaration.setScholarship(recommend.getScholarship());
//            declaration.setSocialPractice(recommend.getSocialPractice());
            declaration.setPatent(recommend.getPatent());
            declaration.setPerson(recommend.getPerson());
            declaration.setFile(recommend.getFile());
            applyDao.insertDeclaration(declaration);
            data.getMeta().setStatus("200");
            data.getMeta().setMsg("推荐成功");
        } catch (Exception e) {
            e.printStackTrace();
            data.getMeta().setStatus("400");
            data.getMeta().setMsg(e.getMessage());
            result.setData(data);
            return result;
        }
        result.setData(data);
        return result;
    }

    /**
     * 查看所有已推荐的同学，增加一个pic字段
     *
     * @param token
     * @param query
     * @param pagenum
     * @param pagesize
     * @return
     */
    @Override
    public Result view(String token, String query, int pagenum, int pagesize) {
        Result result = new Result();
        ViewData viewData = new ViewData();
        ArrayList<ViewParticipants> participantsArrayList = new ArrayList<>();
        Integer stuId, total;
        List<Participants> participantsList;
        try {
            stuId = applyDao.selectUserId(token);
            if (stuId == null) {
                viewData.getMeta().setStatus("400");
                viewData.getMeta().setMsg("token无效");
                result.setData(viewData);
                return result;
            }
            if (query == null || query.equals("")) {
                participantsList = applyDao.viewParticipants(pagesize, (pagenum - 1) * pagesize);
                total = applyDao.viewParticipantsNum();
            } else {
                participantsList = applyDao.viewParticipantsByQuery(pagesize, (pagenum - 1) * pagesize, query);
                total = applyDao.viewParticipantsNumByQuery(query);
            }
            for (Participants p : participantsList) {
                ViewParticipants viewParticipants = new ViewParticipants();
                String picPath = applyDao.getFilePath(p.getStuId());
                if (picPath != null && !picPath.equals("") && !picPath.contains("https://") && !picPath.contains("http://")) {
                    picPath = "http://" + picPath;
                }
                viewParticipants.setPic(picPath);
                viewParticipants.setStuId(p.getStuId());
                viewParticipants.setName(p.getName());
                viewParticipants.setCollege(p.getCollege());
//                viewParticipants.setStar(p.getStar());
                viewParticipants.setVote(p.getVote());
                viewParticipants.setSex(p.getSex());   //新增：返回性别
                participantsArrayList.add(viewParticipants);
            }
            viewData.setParticipantsList(participantsArrayList);
            viewData.setTotal(total);
            viewData.getMeta().setStatus("200");
            viewData.getMeta().setMsg("查看成功");
        } catch (Exception e) {
            e.printStackTrace();
            viewData.getMeta().setStatus("400");
            viewData.getMeta().setMsg(e.getMessage());
            result.setData(viewData);
            return result;
        }
        result.setData(viewData);
        return result;
    }

    /**
     * 投票
     *
     * @param token
     * @param stuId
     * @return
     */
    @Override
    public Result vote(String token, int stuId) {
        Result result = new Result();
        Data data = new Data();
        Integer voterId;  //投票人id
        String currentDate = CurrentTime.getCurrentTime("yyyy-MM-dd");  //当前日期
        try {
            voterId = applyDao.selectUserId(token);
            if (voterId == null) {
                data.getMeta().setStatus("400");
                data.getMeta().setMsg("token无效");
                result.setData(data);
                return result;
            }
            Vote vote = applyDao.selectVoteInfo(voterId);
            if (vote == null) {
                data.getMeta().setStatus("400");
                data.getMeta().setMsg("用户异常");
                result.setData(data);
                return result;
            } else if (vote.getLastVoteTime() == null || vote.getLastVoteTime().equals("") || !vote.getLastVoteTime().equals(currentDate)) {  //上次投票不是同一天，要将投票数归0，要避免空指针异常，因为可能以前没投过票，是null
                applyDao.updateVoteInfo(currentDate, 1, voterId);
            } else if (vote.getLastVoteTime().equals(currentDate)) {
                if (vote.getVoteNum() == 10) {
                    data.getMeta().setStatus("400");
                    data.getMeta().setMsg("投票次数已满");
                    result.setData(data);
                    return result;
                } else {
                    applyDao.updateVoteInfo(currentDate, vote.getVoteNum() + 1, voterId);
                }
            }
            applyDao.vote(stuId);
            data.getMeta().setStatus("200");
            data.getMeta().setMsg("投票成功");
        } catch (Exception e) {
            e.printStackTrace();
            data.getMeta().setStatus("400");
            data.getMeta().setMsg(e.getMessage());
            result.setData(data);
            return result;
        }
        result.setData(data);
        return result;
    }

//    /**
//     * 不用了！！！
//     * 通过名字来查询“已经推荐”的同学
//     *
//     * @param token
//     * @param name
//     * @return
//     */
//    @Override
//    public Result search(String token, String name) {
//        Result result = new Result();
//        ViewData viewData = new ViewData();
//        ArrayList<ViewParticipants> participantsArrayList = new ArrayList<>();
//        Integer stuId;
//        try {
//            stuId = applyDao.selectUserId(token);
//            if (stuId == null) {
//                viewData.getMeta().setStatus("400");
//                viewData.getMeta().setMsg("token无效");
//                result.setData(viewData);
//                return result;
//            }
//            List<Participants> participantsList = applyDao.viewName(name);
//            if(participantsList.isEmpty()){
//                viewData.getMeta().setStatus("400");
//                viewData.getMeta().setMsg("未找到");
//                result.setData(viewData);
//                return result;
//            }
//            Integer total = applyDao.viewNameNum(name);
//            for (Participants p : participantsList) {
//                ViewParticipants viewParticipants = new ViewParticipants();
//                viewParticipants.setStuId(p.getStuId());
//                viewParticipants.setName(p.getName());
//                viewParticipants.setCollege(p.getCollege());
//                viewParticipants.setStar(p.getStar());
//                viewParticipants.setVote(p.getVote());
//                participantsArrayList.add(viewParticipants);
//            }
//            viewData.setParticipantsList(participantsArrayList);
//            viewData.setTotal(total);
//            viewData.getMeta().setStatus("200");
//            viewData.getMeta().setMsg("搜索成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        result.setData(viewData);
//        return result;
//    }


    /**
     * 学生页面查看参与者的申报材料
     *
     * @param token
     * @param stuId
     * @return
     */
    @Override
    public Result details(String token, int stuId) {
        Result result = new Result();
        ApplyDeclarationData declarationData = new ApplyDeclarationData();
        ArrayList<DeclareInfo> details = new ArrayList<>();
        Participants participants;
        Declaration declaration;
        Integer oriStuId = null;  //查看人的stuId
        try {
            oriStuId = applyDao.selectUserId(token);
            if (oriStuId == null) {
                declarationData.getMeta().setStatus("400");
                declarationData.getMeta().setMsg("token无效");
                result.setData(declarationData);
                return result;
            }
            participants = applyDao.getParticipantsInfo(stuId);
            declaration = applyDao.getDeclaration(stuId);
            if (participants == null || declaration == null) {
                declarationData.getMeta().setStatus("400");
                declarationData.getMeta().setMsg("查询的学生id有误或者该学生没有申报材料");
                result.setData(declarationData);
                return result;
            }

            DeclareInfo declareInfo0 = new DeclareInfo();
            declareInfo0.setInfo("编号");
            declareInfo0.setContent(Integer.toString(participants.getStuId()));
            details.add(declareInfo0);

            DeclareInfo declareInfo1 = new DeclareInfo();
            declareInfo1.setInfo("姓名");
            declareInfo1.setContent(participants.getName());
            details.add(declareInfo1);

            DeclareInfo declareInfo2 = new DeclareInfo();
            declareInfo2.setInfo("学号");
            declareInfo2.setContent(participants.getStuNum());
            details.add(declareInfo2);

            DeclareInfo declareInfo2_ = new DeclareInfo();
            declareInfo2_.setInfo("学院");
            declareInfo2_.setContent(participants.getCollege());
            details.add(declareInfo2_);



//            DeclareInfo declareInfo3 = new DeclareInfo();
//            declareInfo3.setInfo("评选类型");
//            declareInfo3.setContent(participants.getStar());
//            details.add(declareInfo3);


            DeclareInfo declareInfo4 = new DeclareInfo();
            declareInfo4.setInfo("学业学习排名情况");
            declareInfo4.setContent(declaration.getRank());
            details.add(declareInfo4);

//            DeclareInfo declareInfo5 = new DeclareInfo();
//            declareInfo5.setInfo("获奖学金情况");
//            declareInfo5.setContent(declaration.getScholarship());
//            details.add(declareInfo5);

            DeclareInfo declareInfo6 = new DeclareInfo();
            declareInfo6.setInfo("竞赛奖项情况");
            declareInfo6.setContent(declaration.getHonor());
            details.add(declareInfo6);

//            DeclareInfo declareInfo7 = new DeclareInfo();
//            declareInfo7.setInfo("科技竞赛获奖情况");
//            declareInfo7.setContent(declaration.getTechHonor());
//            details.add(declareInfo7);

//            DeclareInfo declareInfo8 = new DeclareInfo();
//            declareInfo8.setInfo("社会实践情况");
//            declareInfo8.setContent(declaration.getSocialPractice());
//            details.add(declareInfo8);
            DeclareInfo declareInfo8 = new DeclareInfo();
            declareInfo8.setInfo("个人荣誉情况");
            declareInfo8.setContent(declaration.getPerson());
            details.add(declareInfo8);

//            DeclareInfo declareInfo9 = new DeclareInfo();
//            declareInfo9.setInfo("等级考试通过情况");
//            declareInfo9.setContent(declaration.getRankTest());
//            details.add(declareInfo9);
            DeclareInfo declareInfo9 = new DeclareInfo();
            declareInfo9.setInfo("专利情况");
            declareInfo9.setContent(declaration.getPatent());
            details.add(declareInfo9);

            DeclareInfo declareInfo10 = new DeclareInfo();
            declareInfo10.setInfo("论文发表情况");
            declareInfo10.setContent(declaration.getPaper());
            details.add(declareInfo10);

//            DeclareInfo declareInfo11 = new DeclareInfo();
//            declareInfo11.setInfo("其他获奖");
//            declareInfo11.setContent(declaration.getOthers());
//            details.add(declareInfo11);
            DeclareInfo declareInfo11 = new DeclareInfo();
            declareInfo11.setInfo("科研项目情况");
            declareInfo11.setContent(declaration.getTechprogram());
            details.add(declareInfo11);

//            DeclareInfo declareInfo12 = new DeclareInfo();
//            declareInfo12.setInfo("先进事迹");
//            declareInfo12.setContent(declaration.getGoodDeeds());
//            details.add(declareInfo12);
            DeclareInfo declareInfo12 = new DeclareInfo();
            declareInfo12.setInfo("推荐原因");
            declareInfo12.setContent(declaration.getReason());
            details.add(declareInfo12);

//            DeclareInfo declareInfo13 = new DeclareInfo();
//            declareInfo13.setInfo("座右铭");
//            declareInfo13.setContent(declaration.getMotto());
//            details.add(declareInfo13);

            declarationData.setDetails(details);
            declarationData.getMeta().setStatus("200");
            declarationData.getMeta().setMsg("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData(declarationData);
        return result;
    }
}