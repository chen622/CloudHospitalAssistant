package cn.neuedu.his.config;

import org.apache.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component//需要@EnableConfigurationProperties
@PropertySource("classpath:application.yml")//使用@PropertySource来指定自定义的资源目录
@ConfigurationProperties(prefix = "spring.redis") //读取application.properties文件中以“spring.redis”开头的变量值。
public class RedisConfig {
    private static Logger logger = Logger.getLogger(RedisConfig.class);
    private String hostName;
    private String password;
    private int port;
    private int timeout;
    private int database;

    public JedisPoolConfig getRedisConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        return config;
    }

    @Bean//@Bean注解将一个配置类的方法的返回值定义为一个bean，注册到spring里面
    public JedisPool getJedisPool(){
        JedisPoolConfig config = getRedisConfig();
        JedisPool pool = new JedisPool(config,hostName,port,timeout,password, database);
        logger.info("init JredisPool ...");
        return pool;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

     public String getPassword() {
        return password;
     }

     public void setPassword(String password) {
        this.password = password;
     }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }
}
