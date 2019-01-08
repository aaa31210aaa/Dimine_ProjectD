package entity;

public class User {
    private String id;
    private String name;
    private String headUrl;
    private String position;

    public User(String id, String name, String headUrl) {
        this.id = id;
        this.name = name;
        this.headUrl = headUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }


    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
