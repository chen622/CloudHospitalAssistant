package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface PaymentRemote {
    @PostMapping("/payment/getAll")
    JSONObject getAll(@RequestBody JSONObject jsonObject);

    @GetMapping("/payment/getByDoctor/{patientId}")
    JSONObject getByDoctor(@PathVariable("patientId") Integer patientId);

    @GetMapping("/payment/getForStatistics")
    JSONObject getForStatistics(@RequestBody JSONObject object);

    @PostMapping("/payment/payRegistration")
    JSONObject payRegistration(@RequestBody JSONObject jsonObject);


    @PostMapping("/payment/pay")
    JSONObject pay(@RequestBody JSONObject jsonObject);

    @PostMapping("/payment/produceRetreatPayment")
    JSONObject produceRetreatPayment(@RequestBody JSONObject jsonObject);

    @PostMapping("/payment/retreatDrugFee/{paymentId}")
    JSONObject retreatDrugFee(@PathVariable("paymentId") Integer paymentId);
}
