package cn.neuedu.server.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name= "eureka-producer")
public interface GeneralRemote {
    @GetMapping("/getAllDepartmentKind")
    JSONObject getAllDepartmentKind();
}
