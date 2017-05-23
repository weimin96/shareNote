package com.aoliao.notebook.model;


/**
 * Created by 你的奥利奥 on 2017/2/11.
 */
public class Note {
    private int id;
    private String title;
    private String content;
    private Long editTime;

    public Note(String title, String content, Long editTime,int id) {
        this.content = content;
        this.editTime = editTime;
        this.title = title;
        this.id=id;
    }

    public String getContent() {
        return content;
    }

    public Long getEditTime() {
        return editTime;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEditTime(Long editTime) {
        this.editTime = editTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
