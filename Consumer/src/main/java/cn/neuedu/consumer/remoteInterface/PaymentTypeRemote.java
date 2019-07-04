package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface PaymentTypeRemote {
    @PostMapping("/payment_type/insertPaymentType")
    JSONObject insertPaymentType(@RequestBody JSONObject jsonObject) throws Exception;

    @PostMapping("/payment_type/deletePaymentType/{id}")
    JSONObject deletePaymentType(@PathVariable("id") Integer id);


    @PostMapping("/payment_type/recoverPaymentType")
    JSONObject recoverPaymentType(@RequestBody JSONObject object);

    @PostMapping("/payment_type/modifyPaymentType")
    JSONObject modifyPaymentType(@RequestBody JSONObject jsonObject);


    @PostMapping("/payment_type/updatePaymentType")
    JSONObject updatePaymentType(@RequestBody JSONObject jsonObject);


    @GetMapping("/payment_type/selectPaymentType/{paymentTypeName}")
    JSONObject selectPaymentType(@PathVariable("paymentTypeName") String paymentTypeName);

    @GetMapping("/payment_type/getAll")
    JSONObject getAll();

    @GetMapping("/payment_type/getByTypeId/{typeId}")
    JSONObject getByType(@PathVariable("typeId") Integer typeId);
}
