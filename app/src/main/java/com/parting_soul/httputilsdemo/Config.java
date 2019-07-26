package com.parting_soul.httputilsdemo;

/**
 * @author parting_soul
 * @date 2019-07-23
 */
public class Config {
    public static final String BASE_URL = "XXXX";


    public static final String UPLOAD_IMG = BASE_URL + "/user/update/user/info";
    public static final String LOGIN = BASE_URL + "/login/mobile";
    public static final String ADD_TO_SHELF = BASE_URL + "/api/add/bookcase";
    public static final String KEY_SESSION = "key_session";
    public static final String BOUNDARY = "boundary";


    public static void saveUserInfo(LoginBean bean) {
        SPUtil.putString(KEY_SESSION, bean.getData().getSession());
    }

    public static String getSession() {
        return SPUtil.getString(KEY_SESSION);
    }

}
