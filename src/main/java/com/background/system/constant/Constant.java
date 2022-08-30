package com.background.system.constant;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/20 14:17
 */
public class Constant {

    public static final String ALLOW_URL = "/error";

    public static final String TOKEN_KEY = "Authorization";

    public static final String ADMIN_REQUEST_TYPE = "/admin";

    public static final String SWAGGER_TYPE = "/swagger";

    public static final String WX_TOKEN_KEY = "open_id";

    public static final String USER_NAME = "user_name";

    public static final String PASSWORD = "password";

    public static final Long EXPIRE_TIME = System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15;
}
