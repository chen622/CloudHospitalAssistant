package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface DiseaseFirstRemote {
    @PostMapping("/disease_first/delete/{id}")
    JSONObject deleteDiseaseFirst(@PathVariable Integer id);

    @PostMapping("/disease_first/insert")
    JSONObject insertDiseaseFirst(@RequestBody JSONObject jsonObject);

    @PostMapping("/disease_first/modify")
    JSONObject modifyDiseaseFirst(@RequestBody JSONObject jsonObject);

    @PostMapping("/disease_first/select/{name}")
    JSONObject selectDiseaseFirst(@PathVariable("name") String name);

    @GetMapping("/getType")
    JSONObject getAllDiseaseType();
}
