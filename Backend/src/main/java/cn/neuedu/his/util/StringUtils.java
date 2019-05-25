package cn.neuedu.his.util;

public class StringUtils {
    public static boolean isEmpty(String str) {
        return null == str || "".equals(str) || "null".equals(str);
    }
}
