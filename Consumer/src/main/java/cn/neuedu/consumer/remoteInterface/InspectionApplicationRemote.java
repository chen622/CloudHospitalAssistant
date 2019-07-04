package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

public interface InspectionApplicationRemote {
    @PostMapping("/inspection_application/saveTemporaryInspection")
    JSONObject saveTemporaryInspection(@RequestBody JSONObject object);

    @PostMapping("/inspection_application/saveTemporaryInspectionDrug")
    JSONObject saveTemporaryInspectionDrug(@RequestBody JSONObject object);

    @GetMapping("/inspection_application/getTemporaryInspection/{registrationId}")
    JSONObject getTemporaryInspection(@PathVariable("registrationId") Integer registrationId);

    @GetMapping("/inspection_application/deleteTemporaryInspection/{registrationId}")
    JSONObject deleteTemporaryInspection(@PathVariable("registrationId") Integer registrationId);

    @PostMapping("/inspection_application/saveInspection")
    JSONObject saveInspection(@RequestBody JSONObject object);

    @PostMapping("/inspection_application/confirmApplication/{id}")
    JSONObject confirmApplication(@PathVariable("id") Integer id);

    @PostMapping("/inspection_application/cancelApplication/{id}")
    JSONObject cancelApplication(@PathVariable("id") Integer id);

    @PostMapping("/inspection_application/entryApplicationResult")
    JSONObject entryApplicationResult(@RequestBody JSONObject jsonObject);


    @GetMapping("/inspection_application/selectPatientInformationByNameOrId/name/{name}")
    JSONObject selectPatientInformationByNameOrId(@PathVariable(value = "name") String name);

    @GetMapping("/inspection_application/selectPatientInformationByNameOrId/id/{id}")
    JSONObject selectPatientInformationByNameOrId(@PathVariable(value = "id") Integer id);

    @GetMapping("/inspection_application/selectPatientInformationByNameOrId/nameAndId/{name}/{id}")
    JSONObject selectPatientInformationByNameOrId(@PathVariable(value = "name") String name, @PathVariable(value = "id") Integer id);

    @GetMapping("/inspection_application/selectPatientInformationByNameOrId")
    JSONObject selectPatientInformationByNameOrId();

    @PostMapping("/inspection_application/upload")
    JSONObject upload(@RequestPart("pic") MultipartFile pic);
}
