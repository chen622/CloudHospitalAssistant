package cn.neuedu.his.util.constants;

import cn.neuedu.his.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * 通用常量类
 */
public class Constants {

    public static final String SUCCESS_CODE = "100";
    public static final String SUCCESS_MSG = "请求成功";

    public static final Long EXPIRY_TIME = 1000 * 60 * 60L;  //过期时间1小时
    public static final String AUTH_LOGIN_URL = "/user/login";

    public static final String JWT_SECRET = "NeueduHisFYY";
    // JWT token defaults
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "his-api";
    public static final String TOKEN_AUDIENCE = "his-app";
    public static final String APP_ID = "wx030a4bae225c8a36";
    public static final String SECRIT_KEY = "0afeedfd1922f294d43d2e30b049857a";


    public enum Usage {
        //静脉滴注
        INTRAVENOUSDRIP(401),
        //静脉可注
        INTRAVENOUSINJECTION(402),
        //肌肉注射
        INTRAMUSCULARINJECTION(403),
        //口服
        ORAL(404),
        //皮下注射
        SUBCUTANEOUSINJECTION(405);
        private int id;

        Usage(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }


    //权限级别
    public static final Integer AUTENTICATIONLEVEL = 5;
    //全级
    public static final Integer HOSPITALLEVEL = 501;
    //科室
    public static final Integer DEPTLEVEL = 502;
    //个人
    public static final Integer PERSONALLEVEL = 503;


    //看诊状态
    public static final Integer REGISTRATIONSTATE = 8;
    //预约
    public static final Integer RESERVATION = 801;
    // 待诊
    public static final Integer WAITING_FOR_TREATMENT = 802;
    //进入诊室
    public static final Integer INSIDE_DOCTOR = 809;
    //初诊
    public static final Integer FIRST_DIAGNOSIS = 803;
    // 疑诊
    public static final Integer SUSPECT = 804;
    //确诊
    public static final Integer FINAL_DIAGNOSIS = 805;
    // 诊毕
    public static final Integer FINISH_DIAGNOSIS = 806;
    //取消
    public static final Integer CANCEL = 807;
    // 预约未至
    public static final Integer ABSENSE = 808;


    //费用类型
    //检查费用类型
    public static final Integer NON_DRUG_PAYMENT_TYPE = 1;
    //处方费用类型
    public static final Integer DRUG_PAYMENT_TYPE = 2;
    //挂号费用类型
    public static final Integer REGISTRATION_PAYMENT_TYPE = 3;
    //处置费用类型
    public static final Integer DEAL_PAYMENT_TYPE = 4;


    //缴费状态
    public static final Integer PAYMENT_STATE = 12;
    //形成订单
    public static final Integer PRODUCE_PAYMENT = 1201;
    //缴费未取药
    public static final Integer HAVE_PAID = 1202;
    //缴费完成
    public static final Integer HAVE_COMPLETED_PAID = 1203;
    //退药未退费
    public static final Integer HAVE_RETURN_DRUG = 1204;
    //已退费
    public static final Integer HAVE_RETREAT = 1205;
    //出现退的情况
    public static final Integer HAPPEN_RETREAT = 1206;
    //已全部退完
    public static final Integer HAPPEN_RETREAT_ALL = 1207;

    //预约挂号员id
    public static final Integer WEB_REGISTRAR_ID = 15;

}
