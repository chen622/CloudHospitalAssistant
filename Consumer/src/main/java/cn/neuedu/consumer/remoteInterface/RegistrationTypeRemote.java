package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface RegistrationTypeRemote {
    @GetMapping("/registration_type/get")
    JSONObject getAll();


    @PostMapping("/registration_type/insertRegisterType")
    JSONObject insertRegisterType(@RequestBody JSONObject jsonObject);


    @PostMapping("/registration_type/deleteRegisterType/{id}")
    JSONObject deleteRegisterType(@PathVariable("id") Integer id);

    @PostMapping("/registration_type/modifyRegisterType")
    JSONObject modifyRegisterType(@RequestBody JSONObject jsonObject);


    @GetMapping("/registration_type/selectRegisterType/{name}")
    JSONObject selectRegisterType(@PathVariable("name") String name);
}
