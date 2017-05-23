/*
 * Copyright 2016 XuJiaji
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aoliao.notebook.model.data;

import com.aoliao.notebook.helper.Config;
import com.aoliao.notebook.model.entity.EditItem;
import com.aoliao.notebook.model.entity.MainTag;
import com.aoliao.notebook.model.entity.Set;
import com.aoliao.notebook.model.entity.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by jiana on 16-11-4.
 */

public class DataFiller {
    public static List<Set> getSetShowData() {
        List<Set> list = new ArrayList<>();
        list.add(new Set("缓存大小", "0.0KB"));
        list.add(new Set("关于", ""));
        return list;
    }


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
    public static List<EditItem> getEditList() {
        User user = getLocalUser();
        if (user == null) {
            return null;
        }
        List<EditItem> editItems = new ArrayList<>();
        editItems.add(Config.euii.HEAD, new EditItem(user.getHeadPic(), "@" + user.getUsername(), "      "));
        editItems.add(Config.euii.NICKNAME, new EditItem("昵称", user.getNickname()));
        editItems.add(Config.euii.SEX, new EditItem("性别", user.getSex() == 0 ? "男" : user.getSex() == 1 ? "女" : "保密"));
        editItems.add(Config.euii.PHONE, new EditItem("手机", user.getMobilePhoneNumber()));
        editItems.add(Config.euii.CITY, new EditItem("城市", user.getCity() == null ? "从星空而来" : user.getCity()));
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
