
package com.aoliao.notebook.utils.entity;

public class EditItem {
    private String headPic;
    private String typeName;
    private String value;

    public EditItem(String typeName, String value) {
        this.typeName = typeName;
        this.value = value;
    }

    public EditItem(String headPic, String value, String typeName) {
        this.headPic = headPic;
        this.value = value;
        this.typeName = typeName;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
