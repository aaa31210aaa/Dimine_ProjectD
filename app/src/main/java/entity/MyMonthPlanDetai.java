package entity;

public class MyMonthPlanDetai {
//    "title":["计划名称","所属项目部","工序","开始位置","作业地点","班组","标识工程","指标一","指标二","指标三","指标四"]
//     "cells":{"detail":[{"planname":"2016-08采矿月生产计划","deptname":"大冶铜绿山矿","techname":"掘进一班","startvalue":"1.1111","projectname":"-其他工程1","teamname":"掘进一班","flagname":"重点工程","colname1":"","colname2":"","colname3":"","colname4":"","colname5":"","colname6":"","colname7":"","colname8":"","colname9":"","colname10":"","colname11":"","colname12":"","colname13":"","colname14":"","colname15":"","colname16":"","colname17":"","colname18":"","colname19":"","colname20":""},{"planname":"2016-07采矿月生产计划","deptname":"大冶铜绿山矿","techname":"掘进一班","startvalue":"1.1111","projectname":"-其他工程1","teamname":"掘进一班","flagname":"重点工程","colname1":"3","colname2":"1","colname3":"1","colname4":"1","colname5":"","colname6":"","colname7":"","colname8":"","colname9":"","colname10":"","colname11":"","colname12":"","colname13":"","colname14":"","colname15":"","colname16":"","colname17":"","colname18":"","colname19":"","colname20":""}
    /**
     * planname
     * deptname
     * techname
     * startvalue
     * projectname
     * teamname
     * flagname
     * colname1
     * colname2
     * colname3
     * colname4
     * colname5
     * colname6
     * colname7
     * colname8
     * colname9
     * colname10
     * colname11
     * colname12
     * colname13
     * colname14
     * colname15
     * colname16
     * colname17
     * colname18
     * colname19
     * colname20
     */

    private String planname;
    private String deptname;
    private String techname;
    private String startvalue;
    private String projectname;
    private String teamname;
    private String flagname;

    public String getPlanname() {
        return planname;
    }

    public void setPlanname(String planname) {
        this.planname = planname;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getTechname() {
        return techname;
    }

    public void setTechname(String techname) {
        this.techname = techname;
    }

    public String getStartvalue() {
        return startvalue;
    }

    public void setStartvalue(String startvalue) {
        this.startvalue = startvalue;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public String getFlagname() {
        return flagname;
    }

    public void setFlagname(String flagname) {
        this.flagname = flagname;
    }
}
