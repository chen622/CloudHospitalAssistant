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


    //临床科室
    public static final  Integer clinicalDepartments=101;
    //医技科室
    public static final  Integer technicalDepartments=102;

    //结算类别
    public static final  Integer technicalDept=2;
    //自费
    public static final  Integer paymentBySelf=201;
    //医保
    public static final  Integer paymentByInsurance=202;
    //新农合
    public static final  Integer paymentByNCMS=203;


    //上午
    public static final  Integer morning=301;
    //上午
    public static final  Integer afternoon=302;
    //全午
    public static final  Integer allDay=303;

    //静脉滴注
    public static final  Integer intravenousDrip=401;
    //静脉可注
    public static final  Integer intravenousInjection=402;
    //肌肉注射
    public static final  Integer intramuscularInjection=403;
    //口服
    public static final  Integer oral=404;
    //皮下注射
    public static final  Integer subcutaneousInjection=405;


    //权限级别
    public static final  Integer autenticationLevel=5;

    //全级
    public static final  Integer hospitalLevel=501;
    //科室
    public static final  Integer deptLevel=502;
    //个人
    public static final  Integer personalLevel=503;



    //用户类别
    public static final  Integer userType=6;
    //挂号收费员
    public static final  Integer registrationClek=601;
    //门诊医生
    public static final  Integer OutpatientDoctor=602;
    //医技医生
    public static final  Integer technicalDoctor=603;
    //药房操作员
    public static final  Integer pharmacyOperator=604;
    //财务管理员
    public static final  Integer financialManager=605;
    //医院管理员
    public static final  Integer hospitalAdministrator=606;


    //主任医师
    public static final  Integer chiefDoctor=701;
    //副主任医师
    public static final  Integer deputyChiefDoctor=702;
    //主治医师
    public static final  Integer attendingDoctor=703;
    //住院医师
    public static final  Integer Resident=704;



    //预约
    public static final  Integer reservation=801;

    // 待诊
    public static final  Integer WaitingForTreatment=802;
    //初诊
    public static final  Integer firstDiagnosis=803;
    // 疑诊
    public static final  Integer Suspect=804;
    //确诊
    public static final  Integer finalDiagnosis=805;
    // 诊毕
    public static final  Integer finshDiagnosis=806;
    //取消
    public static final  Integer cancel=807;
    // 预约未至
    public static final  Integer absense=808;




    //职称
    public static final  Integer title=7;
    //看诊状态
    public static final  Integer registrationState=8;
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
