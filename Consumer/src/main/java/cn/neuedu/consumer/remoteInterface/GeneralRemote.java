package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name= "eureka-producer")
public interface GeneralRemote {
    @GetMapping("/general/getAllDepartmentKind")
    JSONObject getAllDepartmentKind();

    @GetMapping("/constant_variable/get")
    JSONObject get();
}
