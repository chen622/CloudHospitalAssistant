package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.DoctorRemote;
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

import java.util.Date;

@RestController
@RequestMapping("/doctor")
@Import(FeignClientsConfiguration.class)
public class DoctorController {
    private DoctorRemote doctorRemote;

    @Autowired
    public DoctorController(
            Decoder decoder, Encoder encoder, Client client) {
        this.doctorRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(DoctorRemote.class, "http://eureka-producer");
    }

    @GetMapping("/getByDepartmentId/{id}")
    public JSONObject getByDepartmentId(@PathVariable("id") Integer departmentId) {
        return doctorRemote.getByDepartmentId(departmentId);
    }
    
    @GetMapping("/getPrescriptionAndInspection/{registrationId}")
    public JSONObject getAllByMedical(@PathVariable("registrationId") Integer registrationId) {
        return doctorRemote.getAllByMedical(registrationId);
    }
    
    @GetMapping("/getByName/{name}")
    public JSONObject getRegistrationByPatientName(@PathVariable("name") String name) {
        return doctorRemote.getRegistrationByPatientName(name);
    }

    @GetMapping("/getRegistrationInfo/{time}")
    public JSONObject getRegistrationInfo(@PathVariable("time") Date time) {
        return doctorRemote.getRegistrationInfo(time);
    }
    
    @GetMapping("/getAllRegistration")
    public JSONObject getAllRegistration() {
        return doctorRemote.getAllRegistration();
    }

    @GetMapping("/insideDoc/{registrationId}")
    public JSONObject getInsideDoc(@PathVariable("registrationId") Integer registrationId) {
        return doctorRemote.getInsideDoc(registrationId);
    }
    
    @GetMapping("/findDisease/{name}")
    public JSONObject findDiseaseByName(@PathVariable("name") String name) {
        return doctorRemote.findDiseaseByName(name);
    }

    @GetMapping("/getAllDiseases")
    public JSONObject getAllDiseases() {
        return doctorRemote.getAllDiseases();
    }

   
    @GetMapping("/findNonDrug/{name}")
    public JSONObject findNonDrugByName(@PathVariable("name") String name) {
        return doctorRemote.findNonDrugByName(name);
    }
    
    @GetMapping("/getAllNonDrug")
    public JSONObject getAllNonDrug() {
        return doctorRemote.getAllNonDrug();
    }
    
    @GetMapping("/openInspection")
    public JSONObject openInspection(JSONObject object) {
        return doctorRemote.openInspection(object);
    }
    
    @GetMapping("/getHospitalCheckTemps")
    public JSONObject getHospitalCheckTemps() {
        return doctorRemote.getHospitalCheckTemps();
    }

    @GetMapping("/getDeptCheckTemps")
    public JSONObject getDeptCheckTemps() {
        return doctorRemote.getDeptCheckTemps();
    }

    @GetMapping("/getPersonalCheckTemps")
    public JSONObject getPersonalInspectionTemps() {
        return doctorRemote.getPersonalInspectionTemps();
    }

   
    @GetMapping("/getInspectionTemByName/{name}")
    public JSONObject getInspectionTemByName(@PathVariable("name") String name) {
        return doctorRemote.getInspectionTemByName(name);
    }

    @PostMapping("/updateInspectionTem")
    public JSONObject updateInspectionTem(@RequestBody JSONObject object) {
        return doctorRemote.updateInspectionTem(object);
    }

    @GetMapping("/deleteInspectionTemp/{id}")
    public JSONObject deleteInspectionTemp(@PathVariable("id") Integer id) {
        return doctorRemote.deleteInspectionTemp(id);
    }

    @GetMapping("/getResult/{id}")
    public JSONObject getInspectionResult(@PathVariable("id") Integer id) {
        return doctorRemote.getInspectionResult(id);
    }

    @PostMapping("/savePrescriptionTemp")
    public JSONObject savePrescriptionsTemp(@RequestBody JSONObject object) {
        return doctorRemote.savePrescriptionsTemp(object);
    }

    @GetMapping("/getPrescriptionsTemByName/{name}")
    public JSONObject getPrescriptionsTemByName(@PathVariable("name") String name) {
        return doctorRemote.getPrescriptionsTemByName(name);
    }

    @GetMapping("/finishDiagnose/{registrationId}")
    public JSONObject finishDiagnose(@PathVariable("registrationId") Integer registrationId) {
        return doctorRemote.finishDiagnose(registrationId);
    }

    @GetMapping("/paymentDetails/{registrationId}/{medicalRecordId}")
    public JSONObject getAllPaymentDetails(@PathVariable("registrationId") Integer registrationId, @PathVariable("medicalRecordId") Integer medicalRecordId) {
        return doctorRemote.getAllPaymentDetails(registrationId, medicalRecordId);
    }
    
    @PostMapping("/getDoctorWorkload")
    public JSONObject getDoctorWorkload(@RequestBody JSONObject jsonObject) {
        return doctorRemote.getDoctorWorkload(jsonObject);
    }

    @PostMapping("/getRStatistics")
    public JSONObject getRStatistics(@RequestBody JSONObject object) {
        return doctorRemote.getRStatistics(object);
    }
}

