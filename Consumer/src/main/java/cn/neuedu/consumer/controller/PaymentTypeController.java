package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.PaymentTypeRemote;
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
@RequestMapping("/payment_type")
@Import(FeignClientsConfiguration.class)
public class PaymentTypeController {
    private PaymentTypeRemote paymentTypeRemote;

    @Autowired
    public PaymentTypeController(
            Decoder decoder, Encoder encoder, Client client) {
        this.paymentTypeRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(PaymentTypeRemote.class, "http://eureka-producer");
    }

    @PostMapping("/insertPaymentType")
    public JSONObject insertPaymentType(@RequestBody JSONObject jsonObject) throws Exception {
        return paymentTypeRemote.insertPaymentType(jsonObject);
    }

    @PostMapping("/deletePaymentType/{id}")
    public JSONObject deletePaymentType(@PathVariable("id") Integer id){
        return paymentTypeRemote.deletePaymentType(id);
    }


    @PostMapping("/recoverPaymentType")
    public JSONObject recoverPaymentType(@RequestBody JSONObject object){
        return paymentTypeRemote.recoverPaymentType(object);
    }

    @PostMapping("/modifyPaymentType")
    public JSONObject modifyPaymentType(@RequestBody JSONObject jsonObject){
        return paymentTypeRemote.modifyPaymentType(jsonObject);
    }


    @PostMapping("/updatePaymentType")
    public JSONObject updatePaymentType(@RequestBody JSONObject jsonObject){
        return paymentTypeRemote.updatePaymentType(jsonObject);
    }

    @GetMapping("/selectPaymentType/{paymentTypeName}")
    public JSONObject selectPaymentType(@PathVariable("paymentTypeName") String  paymentTypeName){
        return paymentTypeRemote.selectPaymentType(paymentTypeName);
    }

    @GetMapping("/getAll")
    public JSONObject getAll(){
        return paymentTypeRemote.getAll();
    }
}
