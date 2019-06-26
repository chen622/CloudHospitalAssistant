package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface MedicalRecordRemote {
    @PostMapping("/medical_record/medical_record/medical_record/saveTemporaryMR")
    JSONObject saveTemporaryMR(@RequestBody JSONObject object);


    @GetMapping("/medical_record/medical_record/getTemporaryMR/{registrationId}")
    JSONObject getTemporaryMR(@PathVariable("registrationId") Integer registrationId);


    @GetMapping("/medical_record/medical_record/deleteTemporaryMR/{registrationId}")
    JSONObject deleteTemporaryMR(@PathVariable("registrationId") Integer registrationId);


    @GetMapping("/medical_record/medical_record/getAllRecord/{patientID}")
    JSONObject getAllRecordByPatientId(@PathVariable("patientID") Integer patientID);
    

    @GetMapping("/medical_record/medical_record/check/{registrationId}")
    JSONObject check(@PathVariable("registrationId") Integer registrationId);

    @PostMapping("/medical_record/medical_record/comein/{registrationId}")
    JSONObject comein(@PathVariable("registrationId") Integer registrationId);


    @PostMapping("/medical_record/medical_record/firstDiagnose")
    JSONObject setFirstDiagnose(@RequestBody JSONObject object);



    @PostMapping("/medical_record/medical_record/finalDiagnose")
    JSONObject saveFinalDiagnose(@RequestBody JSONObject object);


    @PostMapping("/medical_record/medical_record/updateMR")
    JSONObject updateMR(@RequestBody JSONObject object);
}
