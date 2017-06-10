package com.aoliao.notebook.config;

/**
 * 应用参数
 */
public class Config {

    public static final String DATABASE_NAME = "birth_android_api";
    public static final String TABLE_NAME_LOGIN = "login";

    public static final String USERS_USERNAME = "username";
    public static final String USERS_UID = "uid";

    public static class data {
        /**
         * 保存文章数据
         */
        public static final String KEY_POST = "post_key";

        /**
         * 保存用户信息
         */
        public static final String KEY_USER = "user_info";
    }

    /**
     * 个人信息编辑界面对应的id号
     */
    public static class euii {
        /**
         * 头像
         */
        public static final int HEAD = 0;
        //
        public static final int USERNAME = 1;
        /**
         * 昵称
         */
        public static final int NICKNAME = 2;
        /**
         * 性别
         */
        public static final int SEX = 3;
        /**
         * 手机
         */
        public static final int PHONE = 4;
        /**
         * 城市
         */
        public static final int CITY = 5;
        /**
         * 生日
         */
        public static final int BIRTHDAY = 6;
        /**
         * Email
         */
        public static final int EMAIL = 7;
        /**
         * 密码
         */
        public static final int PASSWORD = 8;
        /**
         * 签名
         */
        public static final int SIGN = 9;
    }

    /**
     * SharedPreferences 存储key
     */
    public static class preference {
        /**
         * 文章
         */
        public static final String ARTICLE = "article";
        /**
         * 文章标题
         */
        public static final String ARTICLE_TITLE = "article_title";
    }

    public static final int SEX_SECRET = 2;


}
