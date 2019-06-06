package cn.neuedu.his.util;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
     * @param identifyId
     * @return
     * @throws ParseException
     * @throws InvalidParameterException
     */
    public static Integer identityIdTransferToAge(String identifyId) throws ParseException, InvalidParameterException {
        //通过身份证获取生日
        String birthdayStr = identifyId.substring(6, 14);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date birthday;
        try {
            birthday = format.parse(birthdayStr);
        } catch (ParseException e) {
            throw e;
        }
        System.out.println(birthday);

        ////通过生日获取年龄
        Calendar calendar = Calendar.getInstance();
        if(calendar.getTimeInMillis() - birthday.getTime() < 0L)
            throw new InvalidParameterException();

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
}
