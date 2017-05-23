package com.aoliao.notebook.helper;

/**
 * 应用参数
 */
public class Config {
    public static String URL_LOGIN = "http://10.31.28.201:8080/appServer/LoginServlet";
    public static String URL_REGISTER = "http://10.31.28.201:8080/appServer/RegisterServlet";
    public static String URL_NOTE = "http://10.31.28.201:8080/appServer/NoteServlet";

    public static final String DATABASE_NAME = "birth_android_api";
    public static final String TABLE_NAME_LOGIN = "login";

    public static final String USERS_USERNAME = "username";
    public static final String USERS_PASSWORD = "password";
    public static final String USERS_UID = "uid";
    public static final String USERS_UUID = "uuid";
    public static final String USERS_PHONE = "phone";
    public static final String USERS_CREATE_AT = "created_at";

    public static final String NOTE_CONTENT = "note_content";
    public static final String NOTE_TITLE = "note_title";
    public static final String NOTE_ID = "note_id";
    public static final String NOTE_CREATE_AT = "note_create_at";


    public static final String STATUES = "statues";
    public static final int STATUES_FAIL = 0;
    public static final int STATUES_SUCCESS = 1;

    public static final String ACTION = "action";
    public static final String ACTION_LOGIN = "login";
    public static final String ACTION_REGISTER = "register";
    public static final String ACTION_NOTESAVVE = "note_save";

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
        /**
         * 昵称
         */
        public static final int NICKNAME = 1;
        /**
         * 性别
         */
        public static final int SEX = 2;
        /**
         * 手机
         */
        public static final int PHONE = 3;
        /**
         * 城市
         */
        public static final int CITY = 4;
        /**
         * 生日
         */
        public static final int BIRTHDAY = 5;
        /**
         * Email
         */
        public static final int EMAIL = 6;
        /**
         * 密码
         */
        public static final int PASSWORD = 7;
        /**
         * 签名
         */
        public static final int SIGN = 8;
    }

    /**
     * 首页fragment Key管理
     */
    public static class fragment {
        /**
         * key：主页
         */
        public static final String HOME = "MainFragment";
        /**
         * key：用户信息
         */
        public static final String USER_INFO = "UserInfoFragment";

        /**
         * 用户列表
         */
        public static final String USER_LIST = "UserListFragment";

        /**
         * 编辑个人信息
         */
        public static final String EDIT_USER_INFO = "EditUserInfoFragment";

        /**
         * key：阅读文章
         */
        public static final String READ_ARTICLE = "ReadArticleFragment";

        /**
         * key：登陆
         */

        /**
         * key：注册
         */
        public static final String REGISTER = "RegisterActivity";
        /**
         * key：收藏
         */
        public static final String COLLECT = "CollectFragment";
        /**
         * key：发布
         */
        public static final String RELEASE = "ReleaseFragment";
        /**
         * key：设置
         */
        public static final String SET = "SetFragment";

        /**
         * 关于
         */
        public static final String ABOUT = "AboutFragment";
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
