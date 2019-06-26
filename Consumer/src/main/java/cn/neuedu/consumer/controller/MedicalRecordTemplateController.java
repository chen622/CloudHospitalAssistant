package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.MedicalRecordTemplateRemote;
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
@RequestMapping("/MRT")
@Import(FeignClientsConfiguration.class)
public class MedicalRecordTemplateController {
    private MedicalRecordTemplateRemote medicalRecordTemplateRemote;

    @Autowired
    public MedicalRecordTemplateController(
            Decoder decoder, Encoder encoder, Client client) {
        this.medicalRecordTemplateRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(MedicalRecordTemplateRemote.class, "http://eureka-producer");
    }

    @GetMapping("/getMRT")
    public JSONObject getHospitalMR() {
        return medicalRecordTemplateRemote.getHospitalMR();
    }


    @GetMapping("/getMRTByName/{name}")
    public JSONObject getMedicalRecordTemByName(@PathVariable("name") String name) {
        return medicalRecordTemplateRemote.getMedicalRecordTemByName(name);
    }

    
    @PostMapping("/saveMRT")
    public JSONObject saveMRT(@RequestBody JSONObject object) {
        return medicalRecordTemplateRemote.saveMRT(object);
    }


    @PostMapping("/updateMRT")
    public JSONObject updateMedicalRecordTem(@RequestBody JSONObject object) {
        return medicalRecordTemplateRemote.updateMedicalRecordTem(object);
    }

    @GetMapping("/delete/{id}")
    public JSONObject deleteMedicalRecordTemp(@PathVariable("id") Integer id) {
        return medicalRecordTemplateRemote.deleteMedicalRecordTemp(id);
    }

}
