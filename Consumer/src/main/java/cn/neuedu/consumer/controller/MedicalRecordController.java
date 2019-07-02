package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.MedicalRecordRemote;
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
@RequestMapping("/medical_record")
@Import(FeignClientsConfiguration.class)
public class MedicalRecordController {
    private MedicalRecordRemote medicalRecordRemote;

    @Autowired
    public MedicalRecordController(
            Decoder decoder, Encoder encoder, Client client) {
        this.medicalRecordRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(MedicalRecordRemote.class, "http://eureka-producer");
    }

    @PostMapping("/saveTemporaryMR")
    public JSONObject saveTemporaryMR(@RequestBody JSONObject object) {
        return medicalRecordRemote.saveTemporaryMR(object);
    }

   
    @GetMapping("/getTemporaryMR/{registrationId}")
    public JSONObject getTemporaryMR(@PathVariable("registrationId") Integer registrationId) {
        return medicalRecordRemote.getTemporaryMR(registrationId);
    }

    
    @GetMapping("/deleteTemporaryMR/{registrationId}")
    public JSONObject deleteTemporaryMR(@PathVariable("registrationId") Integer registrationId) {
        return medicalRecordRemote.deleteTemporaryMR(registrationId);
    }

   
    @GetMapping("/getAllRecord/{patientID}")
    public JSONObject getAllRecordByPatientId(@PathVariable("patientID") Integer patientID) {
        return medicalRecordRemote.getAllRecordByPatientId(patientID);
    }


    
    @GetMapping("/check/{registrationId}")
    public JSONObject check(@PathVariable("registrationId") Integer registrationId) {
        return medicalRecordRemote.check(registrationId);
    }

   
    @PostMapping("/comein/{registrationId}")
    public JSONObject comein(@PathVariable("registrationId") Integer registrationId) {
        return medicalRecordRemote.comein(registrationId);
    }

   
    @PostMapping("/firstDiagnose")
    public JSONObject setFirstDiagnose(@RequestBody JSONObject object) {
        return medicalRecordRemote.setFirstDiagnose(object);
    }


    
    @PostMapping("/finalDiagnose")
    public JSONObject saveFinalDiagnose(@RequestBody JSONObject object) {
        return medicalRecordRemote.saveFinalDiagnose(object);
    }

   
    @PostMapping("/updateMR")
    public JSONObject updateMR(@RequestBody JSONObject object) {
        return medicalRecordRemote.updateMR(object);
    }

    @GetMapping("/getAllRecordWithout")
    public JSONObject getAllRecordWithout() {
        return medicalRecordRemote.getAllRecordWithout();
    }

    @GetMapping("/getDrugNonDrugAndResult/{id}")
    public JSONObject getDrugNonDrugAndResultByMedicalId(@PathVariable("id") Integer id) {
        return medicalRecordRemote.getDrugNonDrugAndResultByMedicalId(id);
    }

}
