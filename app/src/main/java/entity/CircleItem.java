package entity;

import android.text.TextUtils;

import java.util.List;


public class CircleItem extends BaseBean {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private String content; //创建的内容
    private String createTime; //创建时间
    private String type;//1:链接  2:图片
    private String linkImg;
    private String linkTitle;
    private List<String> photos;
    private List<FavortItem> favorters;
    private List<CommentItem> comments;
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<FavortItem> getFavorters() {
        return favorters;
    }

    public void setFavorters(List<FavortItem> favorters) {
        this.favorters = favorters;
    }

    public List<CommentItem> getComments() {
        return comments;
    }

    public void setComments(List<CommentItem> comments) {
        this.comments = comments;
    }

    public String getLinkImg() {
        return linkImg;
    }

    public void setLinkImg(String linkImg) {
        this.linkImg = linkImg;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //判断是否有点赞的人
    public boolean hasFavort() {
        if (favorters != null && favorters.size() > 0) {
            return true;
        }
        return false;
    }

    //判断是否有回复的人
    public boolean hasComment() {
        if (comments != null && comments.size() > 0) {
            return true;
        }
        return false;
    }


    //获取点赞人的ID
    public String getCurUserFavortId(String curUserId) {
        String favortid = "";
        if (!TextUtils.isEmpty(curUserId) && hasFavort()) {
            for (FavortItem item : favorters) {
                if (curUserId.equals(item.getUser().getId())) {
                    favortid = item.getId();
                    return favortid;
                }
            }
        }
        return favortid;
    }
}
