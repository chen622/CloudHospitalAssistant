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

    @PostMapping("/constant_variable/insert")
    JSONObject insertConstant(@RequestBody JSONObject jsonObject);

    @PostMapping("/constant_variable/modify")
    JSONObject modifyConstant(@RequestBody JSONObject jsonObject);

    @GetMapping("/delete/{id}")
    JSONObject deleteConstant(@PathVariable("id") Integer id);
}
