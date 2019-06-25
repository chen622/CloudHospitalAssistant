package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface DrugRemote {
    @PostMapping("/drug/takeDrug/{paymentId}/{drugId}")
    JSONObject takeDrug(@PathVariable("paymentId") Integer paymentId, @PathVariable("drugId") Integer drugId);

    @PostMapping("/drug/retreatDrug")
    JSONObject retreatDrug(@RequestBody JSONObject jsonObject);


    @PostMapping("/drug/delete/{id}")
    JSONObject deleteDrug(@PathVariable("id") Integer id);

    @PostMapping("/drug/modify")
    JSONObject modifyDrug(@RequestBody JSONObject jsonObject);

    @PostMapping("/drug/insert")
    JSONObject insertDrug(@RequestBody JSONObject jsonObject);


    @GetMapping("/drug/select/{name}")
    JSONObject selectDrug(@PathVariable("name") String name);

    @GetMapping("/drug/getAllDrugType")
    JSONObject getAllDrugType();

    @GetMapping("/drug/getAllDrug")
    JSONObject getAllDrug();

    @GetMapping("/drug/getAll")
    JSONObject getAll();
}
