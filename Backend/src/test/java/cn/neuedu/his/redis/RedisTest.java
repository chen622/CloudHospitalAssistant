package cn.neuedu.his.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

public class RedisTest {
    @Test
    public void testRedis() {
        Jedis jedis = new Jedis("129.211.61.153", 6379);
        jedis.set("stu", "mitko");
        System.out.println(jedis.get("stu"));
        jedis.close();
    }
}
