package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name= "eureka-producer")
public interface GeneralRemote {
    @GetMapping("/general/getAllDepartmentKind")
    JSONObject getAllDepartmentKind();

    @GetMapping("/general/ip")
    JSONObject ip();
}
