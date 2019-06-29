package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface MedicalRecordTemplateRemote {
    @GetMapping("/MRT/getMRT")
    JSONObject getHospitalMR();


    @GetMapping("/MRT/getMRTByName/{name}")
    JSONObject getMedicalRecordTemByName(@PathVariable("name") String name);


    @PostMapping("/MRT/saveMRT")
    JSONObject saveMRT(@RequestBody JSONObject object);


    @PostMapping("/MRT/updateMRT")
    JSONObject updateMedicalRecordTem(@RequestBody JSONObject object);

    @PostMapping("/MRT/delete/{id}")
    JSONObject deleteMedicalRecordTemp(@PathVariable("id") Integer id);
}
