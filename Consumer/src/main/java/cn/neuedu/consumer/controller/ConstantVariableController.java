package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.ConstantVariableRemote;
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
@RequestMapping("/constant_variable")
@Import(FeignClientsConfiguration.class)
public class ConstantVariableController {
    private ConstantVariableRemote constantVariableRemote;

    @Autowired
    public ConstantVariableController(
            Decoder decoder, Encoder encoder, Client client) {
        this.constantVariableRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(ConstantVariableRemote.class, "http://eureka-producer");
    }

    @GetMapping("/get")
    public JSONObject get() {
        return constantVariableRemote.get();
    }

    @GetMapping("/getForm")
    public JSONObject getDrugForm() {
        return constantVariableRemote.getDrugForm();
    }

    @GetMapping("/getType/{type}")
    public JSONObject getConstantByType(@PathVariable("type") Integer typeId) {
        return constantVariableRemote.getConstantByType(typeId);
    }

    @GetMapping("/getSettlementType")
    public JSONObject getSettlementType() {
        return constantVariableRemote.getSettlementType();
    }


}
