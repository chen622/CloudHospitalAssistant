package cn.neuedu.his.util;

import cn.neuedu.his.model.Patient;
import cn.neuedu.his.model.Registration;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author ccm
 * 字符串处理工具
 */
public class StringUtils {
    public static boolean isEmpty(String str) {
        return null == str || "".equals(str) || "null".equals(str);
    }

    /**
     * 通过身份证获取年龄
     *
     * @param identifyId
     * @return
     * @throws ParseException
     * @throws InvalidParameterException
     */
    public static Integer identityIdTransferToAge(String identifyId) {
        //通过身份证获取生日
        String birthdayStr = identifyId.substring(6, 14);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date birthday;
        try {
            birthday = format.parse(birthdayStr);
        } catch (ParseException e) {
            return 0;
        }
        System.out.println(birthday);

        ////通过生日获取年龄
        Calendar calendar = Calendar.getInstance();
        if (calendar.getTimeInMillis() - birthday.getTime() < 0L)
            return 0;

        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(birthday);
        int yearBirthday = calendar.get(Calendar.YEAR);
        int monthBirthday = calendar.get(Calendar.MONTH);
        int dayOfMonthBirthday = calendar.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirthday;

        if (monthNow < monthBirthday || (monthNow == monthBirthday && dayOfMonthNow < dayOfMonthBirthday)) {
            age--;
        }

        return age;
    }

    /**
     * 设置年龄
     *
     * @param patients
     * @return
     */
    public static JSONArray setAgeForPatientArray(List<Patient> patients) {
        JSONArray jsonArray = new JSONArray();
        for (Patient patient : patients) {
            Integer age = StringUtils.identityIdTransferToAge(patient.getIdentityId());
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(patient);
            jsonObject.put("age", age);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 设置年龄
     *
     * @return
     */
    public static List<Registration> setAgeForRegistrationArray(List<Registration> list) {
        list.forEach(registration -> {
            registration.setAge(identityIdTransferToAge(registration.getPatient().getIdentityId()));
        });
        return list;
    }

    public static Integer castSundayToMonday(Integer day) {
        if (day >= 2) {
            return day - 1;
        } else {
            return 7;
        }
    }
}
