package entity;

public class Notification {

    /**
     * title
     * createdate
     * content
     * messageid
     * mestype
     * mesfile
     * mestime
     * fileurl
     * addname
     * shorttile
     */

    private String title;
    private String createdate;
    private String content;
    private String messageid;
    private String mestype;
    private String mesfile;
    private String mestime;
    private String fileurl;
    private String addname;
    private String shorttile;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getMestype() {
        return mestype;
    }

    public void setMestype(String mestype) {
        this.mestype = mestype;
    }

    public String getMesfile() {
        return mesfile;
    }

    public void setMesfile(String mesfile) {
        this.mesfile = mesfile;
    }

    public String getMestime() {
        return mestime;
    }

    public void setMestime(String mestime) {
        this.mestime = mestime;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getAddname() {
        return addname;
    }

    public void setAddname(String addname) {
        this.addname = addname;
    }

    public String getShorttile() {
        return shorttile;
    }

    public void setShorttile(String shorttile) {
        this.shorttile = shorttile;
    }
}
