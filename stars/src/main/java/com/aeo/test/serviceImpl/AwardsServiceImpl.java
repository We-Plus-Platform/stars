package com.aeo.test.serviceImpl;

import com.aeo.test.bean.*;
import com.aeo.test.dao.AwardsDao;
import com.aeo.test.service.AwardsService;
import com.aeo.tools.CurrentTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(rollbackFor = RuntimeException.class)
@Service
public class AwardsServiceImpl implements AwardsService {
    @Autowired
    AwardsDao awardsDao;

    /**
     * 返回参与者信息
     *
     * @param token
     * @param query
     * @param pagenum
     * @param pagesize
     * @return
     */
    @Override
    public Result returnParticipants(String token, String query, int pagenum, int pagesize) {
        Result result = new Result();
        ParticipantsData participantsData = new ParticipantsData();
        Integer roleId, total;
        List<Participants> participantsList0;
        try {
            roleId = awardsDao.getRoleId(token);
            if (roleId == null) {
                participantsData.getMeta().setStatus("400");
                participantsData.getMeta().setMsg("token无效");
                result.setData(participantsData);
                return result;
            }
            if(query == null || query.equals("")){
                if (roleId == 0) { //超级管理员
                    participantsList0 = awardsDao.getAllParticipants(pagesize, (pagenum - 1) * pagesize);
                    total = awardsDao.getAllParticipantsNum();
                } else {
                    participantsList0 = awardsDao.getParticipants(roleId, pagesize, (pagenum - 1) * pagesize);
                    total = awardsDao.getParticipantsNum(roleId);
                }
            }else{
                if (roleId == 0) { //超级管理员
                    participantsList0 = awardsDao.getAllParticipantsByQuery(query,pagesize, (pagenum - 1) * pagesize);
                    total = awardsDao.getAllParticipantsNumByQuery(query);
                } else {
                    participantsList0 = awardsDao.getParticipantsByQuery(roleId,query, pagesize, (pagenum - 1) * pagesize);
                    total = awardsDao.getParticipantsNumByQuery(roleId,query);
                }
            }

            if (participantsList0.isEmpty()) {
                participantsData.getMeta().setStatus("400");
                participantsData.getMeta().setMsg("无更多用户");
                result.setData(participantsData);
                return result;
            }
            ArrayList<ParticipantsList> participantsList = new ArrayList<>();
            for (Participants p : participantsList0) {
                ParticipantsList temp = new ParticipantsList();
                temp.setAddTime(p.getAddTime());
                temp.setCollege(p.getCollege());
                temp.setName(p.getName());
//                temp.setStar(p.getStar());
                temp.setStatus(Integer.toString(p.getStatus()));
                temp.setTheWay(p.getTheWay());
                temp.setStuNum(p.getStuNum());
                temp.setStuId(p.getStuId());
                Integer times = awardsDao.getTimes(p.getStuNum());
                temp.setTimes(times);
                participantsList.add(temp);
            }
            participantsData.setParticipantsList(participantsList);
            participantsData.setTotal(total);
            participantsData.getMeta().setStatus("200");
            participantsData.getMeta().setMsg("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData(participantsData);
        return result;
    }

    /**
     * 返回参与者申报材料信息
     *
     * @param stuId
     * @param token
     * @return
     */
    @Override
    public Result returnDeclaration(int stuId, String token) {
        Result result = new Result();
        DeclarationData declarationData = new DeclarationData();
        ArrayList<DeclareInfo> declarationInfo = new ArrayList<>();
        Participants participants ;
        Declaration declaration ;
        Integer roleId;
        try {
            roleId = awardsDao.getRoleId(token);
            if (roleId == null) {
                declarationData.getMeta().setStatus("400");
                declarationData.getMeta().setMsg("token无效");
                result.setData(declarationData);
                return result;
            }
            participants = awardsDao.getParticipantsInfo(stuId);
            declaration = awardsDao.getDeclaration(stuId);
            if(participants == null || declaration == null){
                declarationData.getMeta().setStatus("400");
                declarationData.getMeta().setMsg("查询的学生id有误或者该学生没有申报材料");
                result.setData(declarationData);
                return result;
            }
            DeclareInfo declareInfo1 = new DeclareInfo();
            declareInfo1.setInfo("姓名");
            declareInfo1.setContent(participants.getName());
            declarationInfo.add(declareInfo1);

            DeclareInfo declareInfo2 = new DeclareInfo();
            declareInfo2.setInfo("学号");
            declareInfo2.setContent(participants.getStuNum());
            declarationInfo.add(declareInfo2);

//            DeclareInfo declareInfo3 = new DeclareInfo();
//            declareInfo3.setInfo("评选类型");
//            declareInfo3.setContent(participants.getStar());
//            declarationInfo.add(declareInfo3);

            DeclareInfo declareInfo4 = new DeclareInfo();
            declareInfo4.setInfo("学业学习排名情况");
            declareInfo4.setContent(declaration.getRank());
            declarationInfo.add(declareInfo4);

//            DeclareInfo declareInfo5 = new DeclareInfo();
//            declareInfo5.setInfo("获奖学金情况");
//            declareInfo5.setContent(declaration.getScholarship());
//            declarationInfo.add(declareInfo5);

            DeclareInfo declareInfo6 = new DeclareInfo();
            declareInfo6.setInfo("竞赛奖项情况");
            declareInfo6.setContent(declaration.getHonor());
            declarationInfo.add(declareInfo6);

//            DeclareInfo declareInfo7 = new DeclareInfo();
//            declareInfo7.setInfo("科技竞赛获奖情况");
//            declareInfo7.setContent(declaration.getTechHonor());
//            declarationInfo.add(declareInfo7);

//            DeclareInfo declareInfo8 = new DeclareInfo();
//            declareInfo8.setInfo("社会实践情况");
//            declareInfo8.setContent(declaration.getSocialPractice());
//            declarationInfo.add(declareInfo8);
            DeclareInfo declareInfo8 = new DeclareInfo();
            declareInfo8.setInfo("个人荣誉情况");
            declareInfo8.setContent(declaration.getPerson());
            declarationInfo.add(declareInfo8);

//            DeclareInfo declareInfo9 = new DeclareInfo();
//            declareInfo9.setInfo("等级考试通过情况");
//            declareInfo9.setContent(declaration.getRankTest());
//            declarationInfo.add(declareInfo9);
            DeclareInfo declareInfo9 = new DeclareInfo();
            declareInfo9.setInfo("专利情况");
            declareInfo9.setContent(declaration.getPatent());
            declarationInfo.add(declareInfo9);

            DeclareInfo declareInfo10 = new DeclareInfo();
            declareInfo10.setInfo("论文发表情况");
            declareInfo10.setContent(declaration.getPaper());
            declarationInfo.add(declareInfo10);

//            DeclareInfo declareInfo11 = new DeclareInfo();
//            declareInfo11.setInfo("其他获奖");
//            declareInfo11.setContent(declaration.getOthers());
//            declarationInfo.add(declareInfo11);
            DeclareInfo declareInfo11 = new DeclareInfo();
            declareInfo11.setInfo("科研项目情况");
            declareInfo11.setContent(declaration.getTechprogram());
            declarationInfo.add(declareInfo11);

//            DeclareInfo declareInfo12 = new DeclareInfo();
//            declareInfo12.setInfo("先进事迹");
//            declareInfo12.setContent(declaration.getGoodDeeds());
//            declarationInfo.add(declareInfo12);
            DeclareInfo declareInfo12 = new DeclareInfo();
            declareInfo12.setInfo("推荐原因");
            declareInfo12.setContent(declaration.getReason());
            declarationInfo.add(declareInfo12);

//            DeclareInfo declareInfo13 = new DeclareInfo();
//            declareInfo13.setInfo("座右铭");
//            declareInfo13.setContent(declaration.getMotto());
//            declarationInfo.add(declareInfo13);

            DeclareInfo declareInfo14 = new DeclareInfo();
            declareInfo14.setInfo("联系方式");
            declareInfo14.setContent(participants.getphonenum());
            declarationInfo.add(declareInfo14);

            DeclareInfo declareInfo15 = new DeclareInfo();
            declareInfo15.setInfo("辅导员");
            declareInfo15.setContent(participants.getAssistant());
            declarationInfo.add(declareInfo15);

            declarationData.setDeclarationInfo(declarationInfo);
            declarationData.getMeta().setStatus("200");
            declarationData.getMeta().setMsg("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData(declarationData);
        return result;
    }

    /**
     * 审核结果
     * @param stuId
     * @param token
     * @param verifyResult
     * @return
     */
    @Override
    public Result verify(int stuId, String token, VerifyResult verifyResult) {
        Result result = new Result();
        Data data = new Data();
        Integer roleId,userId,collegeId;
        try{
            userId = awardsDao.getUserId(token);
            if(userId == null){
                data.getMeta().setStatus("400");
                data.getMeta().setMsg("token无效");
                result.setData(data);
                return result;
            }
            roleId = awardsDao.getRoleId(token);
            if(!roleId.equals(0)){    //非超级管理员
                collegeId = awardsDao.getCollegeId(stuId);
                if(!collegeId.equals(roleId)){
                    data.getMeta().setStatus("400");
                    data.getMeta().setMsg("无权限操作此学生");
                    result.setData(data);
                    return result;
                }
            }
            String stuNum = awardsDao.getStuNum(stuId);
            if(stuNum == null || stuNum.equals("")){
                data.getMeta().setStatus("400");
                data.getMeta().setMsg("学生编号有误");
                result.setData(data);
                return result;
            }
            if(verifyResult.getNewStatus() == 1){   //判断是否已经推荐过了
                Integer times = awardsDao.verifyTime(stuNum);
                if(times > 0){
                    data.getMeta().setStatus("401");
                    data.getMeta().setMsg("该同学已推荐");
                    result.setData(data);
                    return result;
                }
            }
            String username = awardsDao.getUsername(userId);
            String verifyTime = CurrentTime.getCurrentTime("yyyy-MM-dd HH:mm:ss");
            Integer res = awardsDao.updateStatus(verifyResult.getNewStatus(),username,stuId,verifyTime);
            if(res == 0){
                data.getMeta().setStatus("400");
                data.getMeta().setMsg("更新状态失败");
                result.setData(data);
                return result;
            }
            data.getMeta().setStatus("200");
            data.getMeta().setMsg("更新状态成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData(data);
        return result;
    }

    /**
     * 审核记录，普通管理员无法查看，admin直接看所有
     * @param token
     * @param query
     * @param pagenum
     * @param pagesize
     * @return
     */
    @Override
    public Result recording(String token, String query, int pagenum, int pagesize) {
        Result result = new Result();
        RecordingData recordingData = new RecordingData();
        ArrayList<RecordList> recordList = new ArrayList<>();
        List<Participants> participantsList;
        Integer roleId;
        Integer total;
        try{
            roleId = awardsDao.getRoleId(token);
            if (roleId == null || !roleId.equals(0)) {
                recordingData.getMeta().setStatus("400");
                recordingData.getMeta().setMsg("token无效或者越权访问");
                result.setData(recordingData);
                return result;
            }
            if(query == null || query.equals("")){
                participantsList = awardsDao.getRecording(pagesize, (pagenum - 1) * pagesize);
                total = awardsDao.getTotalRecording();
            }else{
                participantsList = awardsDao.getRecordingByQuery(query,pagesize, (pagenum - 1) * pagesize);
                total = awardsDao.getTotalRecordingByQuery(query);
            }
            if(participantsList.isEmpty()){
                recordingData.getMeta().setStatus("400");
                recordingData.getMeta().setMsg("无更多用户");
                result.setData(recordingData);
                return result;
            }
            for(Participants p:participantsList){
                RecordList r = new RecordList();
                r.setStuId(p.getStuId());
                r.setName(p.getName());
                r.setStuNum(p.getStuNum());
                r.setCollege(p.getCollege());
//                r.setStar(p.getStar());
                r.setStatus(Integer.toString(p.getStatus()));
                r.setHandler(p.getHandler());
                r.setAddTime(p.getVerifyTime());   //审核时间
                recordList.add(r);
            }
            recordingData.getMeta().setStatus("200");
            recordingData.getMeta().setMsg("查询记录成功");
            recordingData.setTotal(total);
            recordingData.setRecordList(recordList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData(recordingData);
        return result;
    }

    /**
     * [已推荐，不符合，待处理]
     * @param token
     * @return
     */
    @Override
    public Result report(String token) {
        Result result = new Result();
        ReportData reportData = new ReportData();
        ArrayList<Integer> reportDataList = new ArrayList<>();
        Integer roleId,pass,notPass,wait;
        try{
            roleId = awardsDao.getRoleId(token);
            if (roleId == null) {
                reportData.getMeta().setStatus("400");
                reportData.getMeta().setMsg("token无效");
                result.setData(reportData);
                return result;
            }
            if(roleId == 0){   //超级管理员
                pass = awardsDao.getStatusNum(1);
                notPass = awardsDao.getStatusNum(2);
                wait = awardsDao.getStatusNum(0);
            }else{
                pass = awardsDao.getStatusNumNormal(1,roleId);
                notPass = awardsDao.getStatusNumNormal(2,roleId);
                wait = awardsDao.getStatusNumNormal(0,roleId);
            }
            reportDataList.add(pass);
            reportDataList.add(notPass);
            reportDataList.add(wait);
            reportData.getMeta().setStatus("200");
            reportData.getMeta().setMsg("查询成功");
            reportData.setReportData(reportDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.setData(reportData);
        return result;
    }

    /**
     * 返回申报材料
     * @param stuId
     * @param token
     * @return
     */
    @Override
    public Result getInfo(int stuId, String token) {
        Result result = new Result();
        GetInfoData getInfoData = new GetInfoData();
        ArrayList<DeclarationInfo> declarationInfoArrayList = new ArrayList<>();
        DeclarationInfo declarationInfo = new DeclarationInfo();
        Participants participants ;
        Declaration declaration ;
        Integer roleId;
        try{
            roleId = awardsDao.getRoleId(token);
            if (roleId == null) {
                getInfoData.getMeta().setStatus("400");
                getInfoData.getMeta().setMsg("token无效");
                result.setData(getInfoData);
                return result;
            }
            participants = awardsDao.getParticipantsInfo(stuId);
            declaration = awardsDao.getDeclaration(stuId);
            if(participants == null || declaration == null){
                getInfoData.getMeta().setStatus("400");
                getInfoData.getMeta().setMsg("查询的学生id有误或者该学生没有申报材料");
                result.setData(getInfoData);
                return result;
            }
            declarationInfo.setStuId(participants.getStuId());
            declarationInfo.setName(participants.getName());
            declarationInfo.setStuNum(participants.getStuNum());
//            declarationInfo.setStar(participants.getStar());
            declarationInfo.setRank(declaration.getRank());
//            declarationInfo.setScholarship(declaration.getScholarship());
            declarationInfo.setHonor(declaration.getHonor());
//            declarationInfo.setTechHonor(declaration.getTechHonor());
            declarationInfo.setPerson(declaration.getPerson());
            declarationInfo.setPatent(declaration.getPatent());
//            declarationInfo.setSocialPractice(declaration.getSocialPractice());
//            declarationInfo.setRankTest(declaration.getRankTest());
            declarationInfo.setPaper(declaration.getPaper());
            declarationInfo.setTechprogram(declaration.getTechprogram());
            declarationInfo.setReason(declaration.getReason());
//            declarationInfo.setOthers(declaration.getOthers());
//            declarationInfo.setGoodDeeds(declaration.getGoodDeeds());
//            declarationInfo.setMotto(declaration.getMotto());
            declarationInfo.setphonenum(participants.getphonenum());
            declarationInfo.setAssistant(participants.getAssistant());
            declarationInfoArrayList.add(declarationInfo);
            getInfoData.setDeclarationInfo(declarationInfoArrayList);
            getInfoData.getMeta().setMsg("查询成功");
            getInfoData.getMeta().setStatus("200");
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData(getInfoData);
        return result;
    }

    @Override
    public Result changeInfo(int stuId, String token, DeclarationInfo declarationInfo) {
        Result result = new Result();
        Data data = new Data();
        Integer roleId;
        try{
            roleId = awardsDao.getRoleId(token);
            if (roleId == null) {
                data.getMeta().setStatus("400");
                data.getMeta().setMsg("token无效");
                result.setData(data);
                return result;
            }
            declarationInfo.setStuId(stuId);
            Integer res1 = awardsDao.changeParInfo(declarationInfo);
            Integer res2 = awardsDao.changeDeclareInfo(declarationInfo);
            if(res1.equals(0)||res2.equals(0)){
                data.getMeta().setStatus("400");
                data.getMeta().setMsg("更新信息失败");
                result.setData(data);
                return result;
            }
            data.getMeta().setStatus("200");
            data.getMeta().setMsg("更新信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            data.getMeta().setStatus("400");
            data.getMeta().setMsg(e.getMessage());
        }
        result.setData(data);
        return result;
    }


    /**
     * 返回最新申请的申报信息
     * @param token
     * @param query
     * @param pagenum
     * @param pagesize
     * @return
     */
    @Override
    public Result returnNewParticipants(String token, String query, int pagenum, int pagesize) {
        Result result = new Result();
        ParticipantsData participantsData = new ParticipantsData();
        Integer roleId, total;
        List<Participants> participantsList0;
        try {
            roleId = awardsDao.getRoleId(token);
            if (roleId == null) {
                participantsData.getMeta().setStatus("400");
                participantsData.getMeta().setMsg("token无效");
                result.setData(participantsData);
                return result;
            }
                if (roleId == 0) { //超级管理员
                    participantsList0 = awardsDao.getAllDistinctParticipants(query,pagesize, (pagenum - 1) * pagesize);
                    total = awardsDao.getAllDistinctParticipantsNum(query);
                } else {
                    participantsList0 = awardsDao.getDistinctParticipants(roleId, query,pagesize, (pagenum - 1) * pagesize);
                    total = awardsDao.getDistinctParticipantsNum(roleId,query);
                }
            if (participantsList0.isEmpty()) {
                participantsData.getMeta().setStatus("400");
                participantsData.getMeta().setMsg("无更多用户");
                result.setData(participantsData);
                return result;
            }
            ArrayList<ParticipantsList> participantsList = new ArrayList<>();
            for (Participants p : participantsList0) {
                ParticipantsList temp = new ParticipantsList();
                temp.setAddTime(p.getAddTime());
                temp.setCollege(p.getCollege());
                temp.setName(p.getName());
//                temp.setStar(p.getStar());
                temp.setStatus(Integer.toString(p.getStatus()));
                temp.setTheWay(p.getTheWay());
                temp.setStuNum(p.getStuNum());
                temp.setStuId(p.getStuId());
                Integer times = awardsDao.getTimes(p.getStuNum());
                temp.setTimes(times);
                participantsList.add(temp);
            }
            participantsData.setParticipantsList(participantsList);
            participantsData.setTotal(total);
            participantsData.getMeta().setStatus("200");
            participantsData.getMeta().setMsg("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            participantsData.getMeta().setStatus("400");
            participantsData.getMeta().setMsg(e.getMessage());
        }
        result.setData(participantsData);
        return result;
    }
}
