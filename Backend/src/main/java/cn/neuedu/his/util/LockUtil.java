package cn.neuedu.his.util;

import cn.neuedu.his.service.impl.RedisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class LockUtil {
    @Autowired
    public RedisServiceImpl redisServiceTemp;

    private static RedisServiceImpl redisService;

    @PostConstruct
    public void init() {
        redisService = redisServiceTemp;
    }

    /**
     * 加锁
     * @param key redis key
     * @param expire 过期时间，单位秒
     * @return true:加锁成功，false，加锁失败
     */
    public static boolean lockImplementation(String key, Integer expire) throws UnsupportedOperationException {
        try {
            long value = System.currentTimeMillis() + expire;
            long status = redisService.setnx(key, String.valueOf(value));

            if(status == 1) {
                return true;
            }

            long oldExpireTime = Long.parseLong(redisService.get(key));
            if(oldExpireTime < System.currentTimeMillis()) {
                //超时
                long newExpireTime = System.currentTimeMillis() + expire;
                long currentExpireTime = Long.parseLong(redisService.getSet(key,
                        String.valueOf(newExpireTime)));
                if(currentExpireTime == oldExpireTime) {
                    return true;
                }
            }
        }catch (Exception e) {
            throw new UnsupportedOperationException("lock");
        }
        return false;
    }

    public static boolean lock(String key, Integer expire) throws UnsupportedOperationException {
        return lockImplementation(key, expire);
    }

    /**
     * 解锁
     * @param key
     * @throws Exception
     */
    public static void unLock(String key) throws UnsupportedOperationException {
        try {
            long oldExpireTime = Long.parseLong(redisService.get(key));
            if (oldExpireTime > System.currentTimeMillis()) {
                redisService.del(key);
            }
        }catch (Exception e) {
            throw new UnsupportedOperationException("lock");
        }

    }
}
