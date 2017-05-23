package com.aoliao.notebook.model;

/**
 * Created by Administrator on 2017/4/23 0023.
 */

public class Choose {
    private String choose;
    private int imageId;
    public Choose(String choose, int imageId){
        this.choose=choose;
        this.imageId=imageId;
    }

    public String getChoose() {
        return choose;
    }

    public int getImageId() {
        return imageId;
    }
}
