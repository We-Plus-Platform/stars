package com.aeo.test.bean;

public class DeclarationInfo {
    private int stuId;
    private String name;
    private String stuNum;
//    private String star;
//    private String rank;//
//    private String scholarship;
//    private String honor;
//    private String techHonor;
//    private String socialPractice;
//    private String rankTest;
//    private String paper;
//    private String others;
//    private String goodDeeds;
//    private String motto;//
    protected String rank;//排名
    protected String honor;//竞赛奖项
    protected String paper;//论文
//    protected String file;//头像文件地址
    protected String person;//个人荣誉
    protected String patent;//专利
    protected String techprogram;//科研项目
    protected String reason;//推荐原因

    private String phonenum;
    private String assistant;



    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

//    @Override
//    public String toString() {
//        return "DeclarationInfo{" +
//                "stuId=" + stuId +
//                ", name='" + name + '\'' +
//                ", stuNum='" + stuNum + '\'' +
//                ", star='" + star + '\'' +
//                ", rank='" + rank + '\'' +
//                ", scholarship='" + scholarship + '\'' +
//                ", honor='" + honor + '\'' +
//                ", techHonor='" + techHonor + '\'' +
//                ", socialPractice='" + socialPractice + '\'' +
//                ", rankTest='" + rankTest + '\'' +
//                ", paper='" + paper + '\'' +
//                ", others='" + others + '\'' +
//                ", goodDeeds='" + goodDeeds + '\'' +
//                ", motto='" + motto + '\'' +
//                ", phonenum='" + phonenum + '\'' +
//                ", assistant='" + assistant + '\'' +
//                '}';
//    }


    @Override
    public String toString() {
        return "DeclarationInfo{" +
                "stuId=" + stuId +
                ", name='" + name + '\'' +
                ", stuNum='" + stuNum + '\'' +
                ", rank='" + rank + '\'' +
                ", honor='" + honor + '\'' +
                ", paper='" + paper + '\'' +
                ", person='" + person + '\'' +
                ", patent='" + patent + '\'' +
                ", techprogram='" + techprogram + '\'' +
                ", reason='" + reason + '\'' +
                ", phonenum='" + phonenum + '\'' +
                ", assistant='" + assistant + '\'' +
                '}';
    }

    //    public static void main(String[] args) {
//        DeclarationInfo a = new DeclarationInfo();
//        System.out.println(a.toString());
//    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStuNum() {
        return stuNum;
    }

    public void setStuNum(String stuNum) {
        this.stuNum = stuNum;
    }

//    public String getStar() {
//        return star;
//    }
//
//    public void setStar(String star) {
//        this.star = star;
//    }

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

    public String getphonenum() {
        return phonenum;
    }

    public void setphonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getAssistant() {
        return assistant;
    }

    public void setAssistant(String assistant) {
        this.assistant = assistant;
    }

    public String getTechprogram() {
        return techprogram;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getPerson() {
        return person;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setTechprogram(String techprogram) {
        this.techprogram = techprogram;
    }

    public String getPatent() {
        return patent;
    }

    public void setPatent(String patent) {
        this.patent = patent;
    }
}
