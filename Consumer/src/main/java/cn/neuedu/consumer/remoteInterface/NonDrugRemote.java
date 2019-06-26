package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface NonDrugRemote {
    @PostMapping("/non_drug/insert")
    JSONObject insertNonDrug(@RequestBody JSONObject jsonObject);


    @GetMapping("/non_drug/selectByName/{name}")
    JSONObject selectNonDrugByName(@PathVariable("name") String name);

    @GetMapping("/non_drug/selectByCode/{code}")
    JSONObject selectNonDrugByCode(@PathVariable("code") String code);

    @PostMapping("/non_drug/delete/{id}")
    JSONObject deleteNonDrug(@PathVariable("id") Integer id);

    @PostMapping("/non_drug/modify")
    JSONObject modifyNonDrug(@RequestBody JSONObject jsonObject);


    @GetMapping("/non_drug/getTypeAndNonDrug/")
    JSONObject getTypeAndNonDrug();
}
