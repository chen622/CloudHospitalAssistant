package cn.neuedu.his;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@tk.mybatis.spring.annotation.MapperScan(basePackages = "cn.neuedu.his.mapper")
public class CloudHospitalAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudHospitalAssistantApplication.class, args);
    }

}

