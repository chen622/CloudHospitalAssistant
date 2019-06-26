package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.PrescriptionRemote;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prescription")
@Import(FeignClientsConfiguration.class)
public class PrescriptionController {
    private PrescriptionRemote prescriptionRemote;

    @Autowired
    public PrescriptionController(
            Decoder decoder, Encoder encoder, Client client) {
        this.prescriptionRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(PrescriptionRemote.class, "http://eureka-producer");
    }

    @PostMapping("/savePrescriptions")
    public JSONObject savePrescriptions(@RequestBody JSONObject object) {
        return prescriptionRemote.savePrescriptions(object);
    }
}
