package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.PaymentRemote;
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
@RequestMapping("/payment")
@Import(FeignClientsConfiguration.class)
public class PaymentController {
    private PaymentRemote paymentRemote;

    @Autowired
    public PaymentController(
            Decoder decoder, Encoder encoder, Client client) {
        this.paymentRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(PaymentRemote.class, "http://eureka-producer");
    }
    
    @PostMapping("/getAll")
    public JSONObject getAll(@RequestBody JSONObject jsonObject) {
        return paymentRemote.getAll(jsonObject);
    }

    @GetMapping("/getByDoctor/{patientId}")
    public JSONObject getByDoctor(@PathVariable("patientId") Integer patientId) {
        return paymentRemote.getByDoctor(patientId);
    }
    
    @GetMapping("/getForStatistics")
    public JSONObject getForStatistics(@RequestBody JSONObject object) {
        return paymentRemote.getForStatistics(object);
    }
    
    @PostMapping("/payRegistration")
    public JSONObject payRegistration(@RequestBody JSONObject jsonObject) {
        return paymentRemote.payRegistration(jsonObject);
    }

    
    @PostMapping("/pay")
    public JSONObject pay(@RequestBody JSONObject jsonObject) {
        return paymentRemote.pay(jsonObject);
    }
    
    @PostMapping("/produceRetreatPayment")
    public JSONObject produceRetreatPayment(@RequestBody JSONObject jsonObject) {
        return paymentRemote.produceRetreatPayment(jsonObject);
    }
    
    @PostMapping("/retreatDrugFee/{paymentId}")
    public JSONObject retreatDrugFee(@PathVariable("paymentId") Integer paymentId) {
        return paymentRemote.retreatDrugFee(paymentId);
    }
}
