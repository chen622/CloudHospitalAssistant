package cn.neuedu.his.util;

/**
 * @author ccm
 * 字符串处理工具
 */
public class StringUtils {
    public static boolean isEmpty(String str) {
        return null == str || "".equals(str) || "null".equals(str);
    }
}
