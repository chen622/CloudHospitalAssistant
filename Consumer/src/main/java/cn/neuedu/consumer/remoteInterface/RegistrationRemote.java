package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface RegistrationRemote {
    @PostMapping("/registration/registration")
    JSONObject registration(@RequestBody JSONObject jsonObject);

    @PostMapping("/registration/preRegistration/{scheduleId}")
    JSONObject preRegistration(@PathVariable("scheduleId") Integer scheduleId);

    @PostMapping("/registration/confirmPre/{registrationId}")
    JSONObject confirmPre(@PathVariable("registrationId") Integer registrationId);

    @PostMapping("/registration/retreat/{registrationId}")
    JSONObject retreatRegistration(@PathVariable("registrationId") Integer registrationId);

    @GetMapping("/registration/getWaitingRegistration/{patientId}")
    JSONObject getWaitingRegistration(@PathVariable("patientId") Integer patientId);
}
