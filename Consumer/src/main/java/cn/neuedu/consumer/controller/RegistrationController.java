package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.RegistrationRemote;
import cn.neuedu.consumer.util.FeignRequestInterceptor;
import com.alibaba.fastjson.JSONObject;
import feign.Client;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
@Import(FeignClientsConfiguration.class)
public class RegistrationController {
    private RegistrationRemote registrationRemote;

    @Autowired
    public RegistrationController(
            Decoder decoder, Encoder encoder, Client client) {
        this.registrationRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(RegistrationRemote.class, "http://eureka-producer");
    }

    @PostMapping("/registration")
    public JSONObject registration(@RequestBody JSONObject jsonObject) {
        return registrationRemote.registration(jsonObject);
    }

    @PostMapping("/preRegistration/{scheduleId}")
    public JSONObject preRegistration(@PathVariable("scheduleId") Integer scheduleId) {
        return registrationRemote.preRegistration(scheduleId);
    }

    @PostMapping("/confirmPre/{registrationId}")
    public JSONObject confirmPre(@PathVariable("registrationId") Integer registrationId) {
        return registrationRemote.confirmPre(registrationId);
    }

    @PostMapping("/retreat/{registrationId}")
    public JSONObject retreatRegistration(@PathVariable("registrationId") Integer registrationId) {
        return registrationRemote.retreatRegistration(registrationId);
    }

    @GetMapping("/getWaitingRegistration/{patientId}")
    public JSONObject getWaitingRegistration(@PathVariable("patientId") Integer patientId) {
        return registrationRemote.getWaitingRegistration(patientId);
    }

}
