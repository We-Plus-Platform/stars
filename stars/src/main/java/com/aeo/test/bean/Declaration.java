package com.aeo.test.bean;

//declaration表的实体
public class Declaration {
    private int stuId;
//    protected String rank;
//    protected String scholarship;
//    protected String honor;
//    protected String techHonor;
//    protected String socialPractice;
//    protected String rankTest;
//    protected String paper;
//    protected String others;
//    protected String goodDeeds;
//    protected String motto;
//    protected String file;
    protected String rank;//排名
    protected String honor;//竞赛奖项
    protected String paper;//论文
    protected String file;//头像文件地址
    protected String person;//个人荣誉
    protected String patent;//专利
    protected String techprogram;//科研项目
    protected String reason;//推荐原因

//    protected String socialPractice;--->person
//    protected String rankTest;--->patent
//    protected String others;--->techprogram
//    protected String goodDeeds;--->reason

//    protected String scholarship;
//    protected String techHonor;
//    protected String motto;



    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

//    public String getScholarship() {
//        return scholarship;
//    }
//
//    public void setScholarship(String scholarship) {
//        this.scholarship = scholarship;
//    }

    public String getHonor() {
        return honor;
    }

    public void setHonor(String honor) {
        this.honor = honor;
    }

//    public String getTechHonor() {
//        return techHonor;
//    }
//
//    public void setTechHonor(String techHonor) {
//        this.techHonor = techHonor;
//    }
//
//    public String getSocialPractice() {
//        return socialPractice;
//    }
//
//    public void setSocialPractice(String socialPractice) {
//        this.socialPractice = socialPractice;
//    }
//
//    public String getRankTest() {
//        return rankTest;
//    }
//
//    public void setRankTest(String rankTest) {
//        this.rankTest = rankTest;
//    }

    public String getPaper() {
        return paper;
    }

    public void setPaper(String paper) {
        this.paper = paper;
    }

//    public String getOthers() {
//        return others;
//    }
//
//    public void setOthers(String others) {
//        this.others = others;
//    }
//
//    public String getGoodDeeds() {
//        return goodDeeds;
//    }
//
//    public void setGoodDeeds(String goodDeeds) {
//        this.goodDeeds = goodDeeds;
//    }
//
//    public String getMotto() {
//        return motto;
//    }
//
//    public void setMotto(String motto) {
//        this.motto = motto;
//    }

    public String getPatent() {
        return patent;
    }

    public void setPatent(String patent) {
        this.patent = patent;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getReason() {
        return reason;
    }

    public void setTechprogram(String techprogram) {
        this.techprogram = techprogram;
    }

    public String getTechprogram() {
        return techprogram;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
