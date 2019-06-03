package cn.neuedu.his;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@EnableTransactionManagement
@SpringBootApplication
@tk.mybatis.spring.annotation.MapperScan(basePackages = "cn.neuedu.his.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class CloudHospitalAssistantApplication {
    private static Logger logger = Logger.getLogger(CloudHospitalAssistantApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CloudHospitalAssistantApplication.class, args);
        logger.info("服务器启动！");
//        ConstantVariableController controller=new ConstantVariableController();
//        System.out.println(controller.getNamebyId(1).toString());


    }


    /**
     * 使用@Bean注入fastJsonHttpMessageConvert
     * @return
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        //1.需要定义一个Convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        //2.添加fastjson的配置信息，比如是否要格式化返回的json数据
//
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        //3.在convert中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);

        return new HttpMessageConverters((HttpMessageConverter<?>) fastConverter);
    }

}

