package entity;

/**
 * 查询历史台账
 */
public class SearchHistory {


    /**
     * teamgroup
     * classes
     * reportdate
     * projectname
     * 指标一
     * 指标二
     */

    private String teamgroup;
    private String classes;
    private String reportdate;
    private String projectname;
    private String 指标一;
    private String 指标二;

    public String getTeamgroup() {
        return teamgroup;
    }

    public void setTeamgroup(String teamgroup) {
        this.teamgroup = teamgroup;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getReportdate() {
        return reportdate;
    }

    public void setReportdate(String reportdate) {
        this.reportdate = reportdate;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String get指标一() {
        return 指标一;
    }

    public void set指标一(String 指标一) {
        this.指标一 = 指标一;
    }

    public String get指标二() {
        return 指标二;
    }

    public void set指标二(String 指标二) {
        this.指标二 = 指标二;
    }
}
