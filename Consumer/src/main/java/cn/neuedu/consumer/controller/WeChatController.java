package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.WeChatRemote;
import cn.neuedu.consumer.util.FeignRequestInterceptor;
import com.alibaba.fastjson.JSON;
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
@RequestMapping("/wechat")
@Import(FeignClientsConfiguration.class)
public class WeChatController {
    private WeChatRemote weChatRemote;

    @Autowired
    public WeChatController(
            Decoder decoder, Encoder encoder, Client client) {
        this.weChatRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(WeChatRemote.class, "http://eureka-producer");
    }

    @PostMapping("/login")
    public JSONObject login(@RequestBody JSONObject requestJson) {
        return weChatRemote.login(requestJson);
    }


    @GetMapping("/getSchedule/{doctorId}")
    public JSONObject getSchedule(@PathVariable("doctorId") Integer doctorId) {
        return weChatRemote.getSchedule(doctorId);
    }

    @GetMapping("/getPatient")
    public JSONObject getPatient() {
        return weChatRemote.getPatient();
    }

    @PostMapping("/updatePatient")
    public JSONObject updatePatient(@RequestBody JSONObject requestJson) {
        return weChatRemote.updatePatient(requestJson);
    }
}
