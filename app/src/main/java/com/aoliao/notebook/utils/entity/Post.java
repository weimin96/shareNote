
package com.aoliao.notebook.utils.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 帖子类
 */

public class Post extends BmobObject {
    /**
     *  帖子标题
     */
    private String title;

    /**
     *  帖子内容
     */
    private String content;

    private Long time;

    private int id;

    public Post(String title, String content, Long editTime,int id) {
        this.content = content;
        this.time = editTime;
        this.title = title;
        this.id=id;
    }
    public Post(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 封面图
     */
    private String coverPicture;

    /**
     *  发布者
     */
    private User author;

    /**
     * 喜欢该帖子的人
     */
    private BmobRelation likes;//多对多关系：用于存储喜欢该帖子的所有用户

    public String getCoverPicture() {
        return coverPicture;
    }

    public void setCoverPicture(String coverPicture) {
        this.coverPicture = coverPicture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", coverPicture='" + coverPicture + '\'' +
                ", author=" + author +
                ", likes=" + likes +
                '}';
    }
}
