package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.PatientRemote;
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
@RequestMapping("/patient")
@Import(FeignClientsConfiguration.class)
public class PatientController {
    private PatientRemote patientRemote;

    @Autowired
    public PatientController(
            Decoder decoder, Encoder encoder, Client client) {
        this.patientRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(PatientRemote.class, "http://eureka-producer");
    }

    @PostMapping("/add")
    public JSONObject registerPatient(@RequestBody JSONObject jsonObject) {
        return patientRemote.registerPatient(jsonObject);
    }

    @GetMapping("/getAllPayment")
    public JSONObject getAllPayment(@RequestBody JSONObject jsonObject) {
        return patientRemote.getAllPayment(jsonObject);
    }

   
    @GetMapping("/getNotPaidPayment/{patientId}")
    public JSONObject getNotPaidPayment(@PathVariable("patientId") Integer patientId) {
        return patientRemote.getNotPaidPayment(patientId);
    }

    
    @GetMapping("/getNotConsumePayment/{patientId}")
    public JSONObject getNotConsumePaymentAndPatient(@PathVariable("patientId") Integer patientId) {
        return patientRemote.getNotConsumePaymentAndPatient(patientId);
    }

   
    @PostMapping("/getDrug")
    public JSONObject getDrugNotTakeInfo(@RequestBody JSONObject jsonObject) {
        return patientRemote.getDrugNotTakeInfo(jsonObject);
    }
    
    @PostMapping("/retreatDrug")
    public JSONObject getDrugRetreatInfo(@RequestBody JSONObject jsonObject) {
        return patientRemote.getDrugRetreatInfo(jsonObject);
    }

    @GetMapping("/getAll")
    public JSONObject findAll() {
        return patientRemote.findAll();
    }

    
    @PostMapping("/searchByMulti")
    public JSONObject selectPatientByIdentifyId(@RequestBody JSONObject json) {
        return patientRemote.selectPatientByIdentifyId(json);
    }

    @GetMapping("/getRegistrations")
    public JSONObject getRegistrations() {
        return patientRemote.getRegistrations();
    }
}
