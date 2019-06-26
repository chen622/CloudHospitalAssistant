package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;

public interface DoctorRemote {
    @GetMapping("/doctor/getByDepartmentId/{id}")
    JSONObject getByDepartmentId(@PathVariable("id") Integer departmentId);

    @GetMapping("/doctor/getPrescriptionAndInspection/{registrationId}")
    JSONObject getAllByMedical(@PathVariable("registrationId") Integer registrationId);


    @GetMapping("/doctor/getByName/{name}")
    JSONObject getRegistrationByPatientName(@PathVariable("name") String name);

    @GetMapping("/doctor/getRegistrationInfo/{time}")
    JSONObject getRegistrationInfo(@PathVariable("time") Date time);

    @GetMapping("/doctor/getAllRegistration")
    JSONObject getAllRegistration();

    @GetMapping("/doctor/insideDoc/{registrationId}")
    JSONObject getInsideDoc(@PathVariable("registrationId") Integer registrationId);

    @GetMapping("/doctor/findDisease/{name}")
    JSONObject findDiseaseByName(@PathVariable("name") String name);

    @GetMapping("/doctor/getAllDiseases")
    JSONObject getAllDiseases();


    @GetMapping("/doctor/findNonDrug/{name}")
    JSONObject findNonDrugByName(@PathVariable("name") String name);

    @GetMapping("/doctor/getAllNonDrug")
    JSONObject getAllNonDrug();

    @GetMapping("/doctor/openInspection")
    JSONObject openInspection(JSONObject object);

    @GetMapping("/doctor/getHospitalCheckTemps")
    JSONObject getHospitalCheckTemps();

    @GetMapping("/doctor/getDeptCheckTemps")
    JSONObject getDeptCheckTemps();

    @GetMapping("/doctor/getPersonalCheckTemps")
    JSONObject getPersonalInspectionTemps();


    @GetMapping("/doctor/getInspectionTemByName/{name}")
    JSONObject getInspectionTemByName(@PathVariable("name") String name);

    @PostMapping("/doctor/updateInspectionTem")
    JSONObject updateInspectionTem(@RequestBody JSONObject object);

    @GetMapping("/doctor/deleteInspectionTemp/{id}")
    JSONObject deleteInspectionTemp(@PathVariable("id") Integer id);

    @GetMapping("/doctor/getResult/{id}")
    JSONObject getInspectionResult(@PathVariable("id") Integer id);

    @PostMapping("/doctor/savePrescriptionTemp")
    JSONObject savePrescriptionsTemp(@RequestBody JSONObject object);

    @GetMapping("/doctor/getPrescriptionsTemByName/{name}")
    JSONObject getPrescriptionsTemByName(@PathVariable("name") String name);

    @GetMapping("/doctor/finishDiagnose/{registrationId}")
    JSONObject finishDiagnose(@PathVariable("registrationId") Integer registrationId);

    @GetMapping("/doctor/paymentDetails/{registrationId}/{medicalRecordId}")
    JSONObject getAllPaymentDetails(@PathVariable("registrationId") Integer registrationId, @PathVariable("medicalRecordId") Integer medicalRecordId);

    @PostMapping("/doctor/getDoctorWorkload")
    JSONObject getDoctorWorkload(@RequestBody JSONObject jsonObject);

    @PostMapping("/doctor/getRStatistics")
    JSONObject getRStatistics(@RequestBody JSONObject object);
}
