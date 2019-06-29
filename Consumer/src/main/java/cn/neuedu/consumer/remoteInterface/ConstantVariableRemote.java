package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

public interface ConstantVariableRemote {
    @GetMapping("/constant_variable/get")
    JSONObject get();

    @GetMapping("/constant_variable/getForm")
    JSONObject getDrugForm();

    @GetMapping("/constant_variable/getType/{type}")
    JSONObject getConstantByType(@PathVariable("type") Integer typeId);

    @GetMapping("/constant_variable/getSettlementType")
    JSONObject getSettlementType();
}
