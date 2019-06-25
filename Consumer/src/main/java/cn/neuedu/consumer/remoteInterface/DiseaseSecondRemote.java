package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface DiseaseSecondRemote {
    @PostMapping("/disease_second/insert")
    JSONObject insertDiseaseSecond(@RequestBody JSONObject jsonObject);

    @PostMapping("/disease_second/delete/{id}")
    JSONObject deleteDiseaseSecond(@PathVariable("id") Integer id);

    @GetMapping("/disease_second/selectByName/{name}")
    JSONObject selectDiseaseSecondByName(@PathVariable("name") String name);

    @GetMapping("/disease_second/selectByIcd/{icdId}")
    JSONObject selectDiseaseSecondByIcd(@PathVariable("icdId") String icdId);

    @PostMapping("/disease_second/modify")
    JSONObject modifyDiseaseSecond(@RequestBody JSONObject jsonObject);

    @GetMapping("/disease_second/getDiseaseByType/{type}")
    JSONObject getDiseaseByType(@PathVariable(value = "type") Integer type);

    @GetMapping("/disease_second/getDiseaseByType")
    JSONObject getDiseaseByType();
}
