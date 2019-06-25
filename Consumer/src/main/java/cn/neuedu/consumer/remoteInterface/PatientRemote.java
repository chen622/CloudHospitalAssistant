package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface PatientRemote {
    @PostMapping("/patient/add")
    JSONObject registerPatient(@RequestBody JSONObject jsonObject);

    @GetMapping("/patient/getAllPayment")
    JSONObject getAllPayment(@RequestBody JSONObject jsonObject);


    @GetMapping("/patient/getNotPaidPayment/{patientId}")
    JSONObject getNotPaidPayment(@PathVariable("patientId") Integer patientId);


    @GetMapping("/patient/getNotConsumePayment/{patientId}")
    JSONObject getNotConsumePaymentAndPatient(@PathVariable("patientId") Integer patientId);


    @PostMapping("/patient/getDrug")
    JSONObject getDrugNotTakeInfo(@RequestBody JSONObject jsonObject);

    @PostMapping("/patient/retreatDrug")
    JSONObject getDrugRetreatInfo(@RequestBody JSONObject jsonObject);

    @GetMapping("/patient/getAll")
    JSONObject findAll();


    @PostMapping("/patient/searchByMulti")
    JSONObject selectPatientByIdentifyId(@RequestBody JSONObject json);
}
