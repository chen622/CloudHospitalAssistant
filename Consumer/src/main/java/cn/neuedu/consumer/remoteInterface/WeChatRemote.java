package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface WeChatRemote {
    @PostMapping("/wechat/login")
    JSONObject login(@RequestBody JSONObject requestJson);

    @GetMapping("/wechat/getSchedule/{doctorId}")
    JSONObject getSchedule(@PathVariable("doctorId") Integer doctorId);

    @GetMapping("/wechat/getPatient")
    JSONObject getPatient();

    @PostMapping("/wechat/updatePatient")
    JSONObject updatePatient(@RequestBody JSONObject requestJson);
}
