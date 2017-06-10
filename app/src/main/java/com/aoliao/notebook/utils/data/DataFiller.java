
package com.aoliao.notebook.utils.data;

import android.util.Log;

import com.aoliao.notebook.config.Config;
import com.aoliao.notebook.utils.entity.EditItem;
import com.aoliao.notebook.utils.entity.Set;
import com.aoliao.notebook.utils.entity.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;


public class DataFiller {

    /**
     * 获取本地用户信息
     */
    public static User getLocalUser() {
        return BmobUser.getCurrentUser(User.class);
    }



    /**
     * 填充编辑个人信息界面显示信息
     * @return
     */
    public static List<EditItem> getEditList(User user) {
//        User user = getLocalUser();
        if (user == null) {
            return null;
        }
        List<EditItem> editItems = new ArrayList<>();
        editItems.add(Config.euii.HEAD, new EditItem(user.getHeadPic(),"    ","头像"));
        editItems.add(Config.euii.USERNAME, new EditItem("用户名", "@" + user.getUsername()));
        editItems.add(Config.euii.NICKNAME, new EditItem("昵称", user.getNickname()));
        editItems.add(Config.euii.SEX, new EditItem("性别", user.getSex() == 0 ? "男" : user.getSex() == 1 ? "女" : "保密"));
        editItems.add(Config.euii.PHONE, new EditItem("手机", user.getMobilePhoneNumber()));
        editItems.add(Config.euii.CITY, new EditItem("地区", user.getCity() == null ? "   " : user.getCity()));
        try{
            editItems.add(Config.euii.BIRTHDAY, new EditItem("生日", SimpleDateFormat.getDateInstance().format(user.getBirthday())));
        }catch (Exception e) {
            editItems.add(Config.euii.BIRTHDAY, new EditItem("生日", "yyyy-MM-dd"));
            e.printStackTrace();
        }
        editItems.add(Config.euii.EMAIL, new EditItem("Email", user.getEmail()));
        editItems.add(Config.euii.PASSWORD, new EditItem("密码", "***"));
        editItems.add(Config.euii.SIGN, new EditItem("签名", user.getSign()));
        return editItems;
    }


}
