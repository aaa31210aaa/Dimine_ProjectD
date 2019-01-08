package entity;

public class Alarm {
    private String alarmtitle; //名称
    private String alarmlevel; //等级
    private String addtime;  //时间
    private String handlecontent; //内容

    public String getAlarmtitle() {
        return alarmtitle;
    }

    public void setAlarmtitle(String alarmtitle) {
        this.alarmtitle = alarmtitle;
    }

    public String getAlarmlevel() {
        return alarmlevel;
    }

    public void setAlarmlevel(String alarmlevel) {
        this.alarmlevel = alarmlevel;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getHandlecontent() {
        return handlecontent;
    }

    public void setHandlecontent(String handlecontent) {
        this.handlecontent = handlecontent;
    }
}
