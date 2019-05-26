package cn.neuedu.his.util.constants;

/**
 *  通用常量类
 */
public class Constants {

    public static final String SUCCESS_CODE = "100";
    public static final String SUCCESS_MSG = "请求成功";

    public static final Long EXPIRY_TIME = 1000 * 60 * 60L;  //过期时间1小时
    public static final String AUTH_LOGIN_URL = "/user/login";
    // Signing key for HS512 algorithm
    // You can use the page http://www.allkeysgenerator.com/ to generate all kinds of keys
    public static final String JWT_SECRET = "NeueduHisFYY";
    // JWT token defaults
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "his-api";
    public static final String TOKEN_AUDIENCE = "his-app";


//    public enum USER_TYPE {
//        CUSTOMER, RESTAURANT, COURIER;
//
//        USER_TYPE() {
//
//        }
//
//        public static USER_TYPE getUserType(String type) {
//            switch (type.toUpperCase()) {
//                case "CUSTOMER":
//                    return CUSTOMER;
//                case "RESTAURANT":
//                    return RESTAURANT;
//                case "COURIER":
//                    return COURIER;
//                default:
//                    return null;
//            }
//        }
//    }
}
