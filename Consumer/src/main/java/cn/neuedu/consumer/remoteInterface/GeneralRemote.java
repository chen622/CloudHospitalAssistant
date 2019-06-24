package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name= "eureka-producer")
public interface GeneralRemote {
    @GetMapping("/general/getAllDepartmentKind")
    JSONObject getAllDepartmentKind();
}
