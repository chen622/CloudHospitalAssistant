package cn.neuedu.his.util.constants;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

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
    public static final List<Integer> userTypeList = Arrays.asList(601,602,603,604,605,606);
    public static final List<Integer> doctorTypeList = Arrays.asList(602,603);


    //临床科室
    public static final  Integer Clinical_Departments=101;
    //医技科室
    public static final  Integer Technical_Departments=102;

    //结算类别
    public static final  Integer Technical_Dept=2;
    //自费
    public static final  Integer Payment_By_Self=201;
    //医保
    public static final  Integer Payment_By_Insurance=202;
    //新农合
    public static final  Integer Payment_By_NCMS=203;


    //上午
    public static final  Integer Morning=301;
    //上午
    public static final  Integer Afternoon=302;
    //全午
    public static final  Integer AllDay=303;

    //静脉滴注
    public static final  Integer IntravenousDrip=401;
    //静脉可注
    public static final  Integer IntravenousInjection=402;
    //肌肉注射
    public static final  Integer IntramuscularInjection=403;
    //口服
    public static final  Integer Oral=404;
    //皮下注射
    public static final  Integer SubcutaneousInjection=405;


    //权限级别
    public static final  Integer AutenticationLevel=5;

    //全级
    public static final  Integer HospitalLevel=501;
    //科室
    public static final  Integer DeptLevel=502;
    //个人
    public static final  Integer PersonalLevel=503;



    //用户类别
    public static final  Integer User_Type=6;
    //挂号收费员
    public static final  Integer Registration_Clerk=601;
    //门诊医生
    public static final  Integer Out_Patient_Doctor=602;
    //医技医生
    public static final  Integer Technical_Doctor=603;
    //药房操作员
    public static final  Integer Pharmacy_Operator=604;
    //财务管理员
    public static final  Integer Financial_Manager=605;
    //医院管理员
    public static final  Integer Hospital_Administrator=606;


    //主任医师
    public static final  Integer Chief_Doctor=701;
    //副主任医师
    public static final  Integer Deputy_Chief_Doctor=702;
    //主治医师
    public static final  Integer Attending_Doctor=703;
    //住院医师
    public static final  Integer Resident=704;



    //预约
    public static final  Integer Reservation=801;

    // 待诊
    public static final  Integer Waiting_For_Treatment=802;
    //初诊
    public static final  Integer First_Diagnosis=803;
    // 疑诊
    public static final  Integer Suspect=804;
    //确诊
    public static final  Integer Final_Diagnosis=805;
    // 诊毕
    public static final  Integer Finsh_Diagnosis=806;
    //取消
    public static final  Integer Cancel=807;
    // 预约未至
    public static final  Integer absense=808;




    //职称
    public static final  Integer Title=7;
    //看诊状态
    public static final  Integer RegistrationState=8;
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
