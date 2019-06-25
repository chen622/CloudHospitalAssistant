package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

//@FeignClient(name= "eureka-producer")
public interface ConstantVariableRemote {
    @GetMapping("/constant_variable/get")
    JSONObject get(Authentication authentication);

    @PostMapping("/constant_variable/getName")
    JSONObject getNameById(@RequestParam(value = "id")Integer id);

    @PostMapping("/insert")
    JSONObject insertConstant(@RequestBody JSONObject jsonObject, @RequestParam(value = "type") String type, Authentication authentication);
}
