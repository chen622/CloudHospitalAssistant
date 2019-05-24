package cn.neuedu.his.util;

import cn.neuedu.his.util.constants.Constants;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @description: 本后台接口系统常用的json工具类
 */
public class CommonUtil {


    /**
     * 返回一个返回码为100的json
     *
     * @param returnData json里的主要内容
     * @return
     */
    public static JSONObject successJson(Object returnData) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("code", Constants.SUCCESS_CODE);
        resultJson.put("msg", Constants.SUCCESS_MSG);
        resultJson.put("data", returnData);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        resultJson.put("token", Jwt.updateToken(request));
        return resultJson;
    }

    public static JSONObject successJsonWithoutToken(Object returnData) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("code", Constants.SUCCESS_CODE);
        resultJson.put("msg", Constants.SUCCESS_MSG);
        resultJson.put("data", returnData);

        return resultJson;
    }


    /**
     * 返回错误信息JSON
     *
     * @param errorEnum 错误码的errorEnum
     * @return
     */
    public static JSONObject errorJson(ErrorEnum errorEnum) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("code", errorEnum.getErrorCode());
        resultJson.put("msg", errorEnum.getErrorMsg());
        resultJson.put("data", new JSONObject());
        return resultJson;
    }

//    public static JSONObject errorJson(ErrorEnum errorEnum, Object object) {
//        JSONObject resultJson = new JSONObject();
//        resultJson.put("code", errorEnum.getErrorCode());
//        resultJson.put("msg", errorEnum.getErrorMsg());
//        resultJson.put("data", object);
//        return resultJson;
//    }


//    /**
//     * 将request参数值转为json
//     *
//     * @param request
//     * @return
//     */
//    public static JSONObject request2Json(HttpServletRequest request) {
//        JSONObject requestJson = new JSONObject();
//        Enumeration paramNames = request.getParameterNames();
//        while (paramNames.hasMoreElements()) {
//            String paramName = (String) paramNames.nextElement();
//            String[] pv = request.getParameterValues(paramName);
//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < pv.length; i++) {
//                if (pv[i].length() > 0) {
//                    if (i > 0) {
//                        sb.append(",");
//                    }
//                    sb.append(pv[i]);
//                }
//            }
//            requestJson.put(paramName, sb.toString());
//        }
//        return requestJson;
//    }

//    /**
//     * 将request转JSON
//     * 并且验证非空字段
//     *
//     * @param request
//     * @param requiredColumns
//     * @return
//     */
//    public static JSONObject convert2JsonAndCheckRequiredColumns(HttpServletRequest request, String requiredColumns) {
//        JSONObject jsonObject = request2Json(request);
//        hasAllRequired(jsonObject, requiredColumns);
//        return jsonObject;
//    }
//
//    /**
//     * 验证是否含有全部必填字段
//     *
//     * @param requiredColumns 必填的参数字段名称 逗号隔开 比如"userId,name,telephone"
//     * @param jsonObject
//     * @return
//     */
//    public static void hasAllRequired(final JSONObject jsonObject, String requiredColumns) {
//        if (!StringTools.isNullOrEmpty(requiredColumns)) {
//            //验证字段非空
//            String[] columns = requiredColumns.split(",");
//            String missCol = "";
//            for (String column : columns) {
//                Object val = jsonObject.get(column.trim());
//                if (StringTools.isNullOrEmpty(val)) {
//                    missCol += column + "  ";
//                }
//            }
//            if (!StringTools.isNullOrEmpty(missCol)) {
//                jsonObject.clear();
//                jsonObject.put("code", ErrorEnum.E_90003.getErrorCode());
//                jsonObject.put("msg", "缺少必填参数:" + missCol.trim());
//                jsonObject.put("data", new JSONObject());
//                throw new CommonJsonException(jsonObject);
//            }
//        }
//    }

}
