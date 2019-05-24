package cn.neuedu.his.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptors {

    // 默认盐，谨慎修改，修改将导致现有数据库密码全部失效
    private final static String DEFAULT_EXTRA_SALT = "3g5DgqE8zIO2WdVb0l0";
    private final static String DEFAULT_ALGORITHM = "SHA";
    /*!!!谨慎修改上述值!!!*/

    /**
     * 不可逆加密
     *
     * @param input 输入字符串
     * @param salt  盐（若null则使用默认盐）
     * @return 加密后字符串
     */
    public static String encrypt(String input, String salt) {
        try {
            if (salt == null) {
                salt = DEFAULT_EXTRA_SALT;
            }
            MessageDigest messageDigest = MessageDigest.getInstance(DEFAULT_ALGORITHM);
            // 混合盐
            messageDigest.update((input + salt + DEFAULT_EXTRA_SALT).getBytes());
            return new BigInteger(messageDigest.digest()).toString(32);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
