package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.RegistrationTypeRemote;
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
@RequestMapping("/registration_type")
@Import(FeignClientsConfiguration.class)
public class RegistrationTypeController {
    private RegistrationTypeRemote registrationTypeRemote;

    @Autowired
    public RegistrationTypeController(
            Decoder decoder, Encoder encoder, Client client) {
        this.registrationTypeRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(RegistrationTypeRemote.class, "http://eureka-producer");
    }
    
    @GetMapping("/get")
    public JSONObject getAll() {
       return registrationTypeRemote.getAll();
    }

    
    @PostMapping("/insertRegisterType")
    public JSONObject insertRegisterType(@RequestBody JSONObject jsonObject) {
        return registrationTypeRemote.insertRegisterType(jsonObject);
    }

   
    @PostMapping("/deleteRegisterType/{id}")
    public JSONObject deleteRegisterType(@PathVariable("id") Integer id) {
        return registrationTypeRemote.deleteRegisterType(id);
    }
    
    @PostMapping("/modifyRegisterType")
    public JSONObject modifyRegisterType(@RequestBody JSONObject jsonObject) {
        return registrationTypeRemote.modifyRegisterType(jsonObject);
    }

    
    @GetMapping("/selectRegisterType/{name}")
    public JSONObject selectRegisterType(@PathVariable("name") String name) {
        return registrationTypeRemote.selectRegisterType(name);
    }
}
