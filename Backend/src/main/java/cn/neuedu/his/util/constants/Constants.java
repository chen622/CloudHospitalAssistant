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
    public static final  Integer CLINICAL_DEPARTMENTS=101;
    //医技科室
    public static final  Integer TECHNICAL_DEPARTMENTS=102;

    //结算类别
    public static final  Integer TECHNICAL_DEPT=2;
    //自费
    public static final  Integer PAYMENT_BY_SELF=201;
    //医保
    public static final  Integer PAYMENT_BY_INSURANCE=202;
    //新农合
    public static final  Integer PAYMENT_BY_NCMS=203;


    //上午
    public static final  Integer MORNING=301;
    //上午
    public static final  Integer AFTERNOON=302;
    //全午
    public static final  Integer ALLDAY=303;

    //静脉滴注
    public static final  Integer INTRAVENOUSDRIP=401;
    //静脉可注
    public static final  Integer INTRAVENOUSINJECTION=402;
    //肌肉注射
    public static final  Integer INTRAMUSCULARINJECTION=403;
    //口服
    public static final  Integer ORAL=404;
    //皮下注射
    public static final  Integer SUBCUTANEOUSINJECTION=405;


    //权限级别
    public static final  Integer AUTENTICATIONLEVEL=5;

    //全级
    public static final  Integer HOSPITALLEVEL=501;
    //科室
    public static final  Integer DEPTLEVEL=502;
    //个人
    public static final  Integer PERSONALLEVEL=503;



    //用户类别
    public static final  Integer USER_TYPE=6;
    //挂号收费员
    public static final  Integer REGISTRATION_CLERK=601;
    //门诊医生
    public static final  Integer OUT_PATIENT_DOCTOR=602;
    //医技医生
    public static final  Integer  TECHNICAL_DOCTOR=603;
    //药房操作员
    public static final  Integer PHARMACY_OPERATOR=604;
    //财务管理员
    public static final  Integer FINANCIAL_MANAGER=605;
    //医院管理员
    public static final  Integer HOSPITAL_ADMINISTRATOR=606;

    //主任医师
    public static final  Integer CHIEF_DOCTOR=701;
    //副主任医师
    public static final  Integer DEPUTY_CHIEF_DOCTOR=702;
    //主治医师
    public static final  Integer ATTENDING_DOCTOR=703;
    //住院医师
    public static final  Integer RESIDENT=704;



    //预约
    public static final  Integer RESERVATION=801;

    // 待诊
    public static final  Integer WAITING_FOR_TREATMENT=802;
    //初诊
    public static final  Integer FIRST_DIAGNOSIS=803;
    // 疑诊
    public static final  Integer SUSPECT=804;
    //确诊
    public static final  Integer FINAL_DIAGNOSIS =805;
    // 诊毕
    public static final  Integer FINISH_DIAGNOSIS =806;
    //取消
    public static final  Integer CANCEL=807;
    // 预约未至
    public static final  Integer ABSENSE=808;

    //挂号费用类型
    public static final Integer REGISTRATION_PAYMENT_TYPE = 0;


    // 非药
    public static final  Integer NON_DRUG =10;
    // 中成药
    public static final  Integer FINISH_TRADITIONAL_DRUG =1101;
    //中草药
    public static final  Integer TRADITIONAL_DRUG=1102;
    // 西药
    public static final  Integer WESTEN_DRUG=1103;


    //职称
    public static final  Integer TITLE=7;
    //看诊状态
    public static final  Integer REGISTRATIONSTATE=8;
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
