package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.InspectionApplicationRemote;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/inspection_application")
@Import(FeignClientsConfiguration.class)
public class InspectionApplicationController {
    private InspectionApplicationRemote inspectionApplicationRemote;

    @Autowired
    public InspectionApplicationController(
            Decoder decoder, Encoder encoder, Client client) {
        this.inspectionApplicationRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(InspectionApplicationRemote.class, "http://eureka-producer");
    }

    @PostMapping("/saveTemporaryInspection")
    public JSONObject saveTemporaryInspection(@RequestBody JSONObject object) {
        return inspectionApplicationRemote.saveTemporaryInspection(object);
    }

    @PostMapping("/saveTemporaryInspectionDrug")
    public JSONObject saveTemporaryInspectionDrug(@RequestBody JSONObject object) {
        return inspectionApplicationRemote.saveTemporaryInspectionDrug(object);
    }

    @GetMapping("/getTemporaryInspection/{registrationId}")
    public JSONObject getTemporaryInspection(@PathVariable("registrationId") Integer registrationId) {
        return inspectionApplicationRemote.getTemporaryInspection(registrationId);
    }

    @GetMapping("/deleteTemporaryInspection/{registrationId}")
    public JSONObject deleteTemporaryInspection(@PathVariable("registrationId") Integer registrationId) {
        return inspectionApplicationRemote.deleteTemporaryInspection(registrationId);
    }

    @PostMapping("/saveInspection")
    public JSONObject saveInspection(@RequestBody JSONObject object) {
        return inspectionApplicationRemote.saveTemporaryInspectionDrug(object);
    }

    @PostMapping("/confirmApplication/{id}")
    public JSONObject confirmApplication(@PathVariable("id") Integer id) {
        return inspectionApplicationRemote.confirmApplication(id);
    }

    @PostMapping("/cancelApplication/{id}")
    public JSONObject cancelApplication(@PathVariable("id") Integer id) {
        return inspectionApplicationRemote.cancelApplication(id);
    }

    @PostMapping("/entryApplicationResult")
    public JSONObject entryApplicationResult(@RequestBody JSONObject jsonObject) {
        return inspectionApplicationRemote.entryApplicationResult(jsonObject);
    }

    @GetMapping("/selectPatientInformationByNameOrId/name/{name}")
    JSONObject selectPatientInformationByNameOrId(@PathVariable(value = "name") String name) {
        return inspectionApplicationRemote.selectPatientInformationByNameOrId(name);
    }

    @GetMapping("/selectPatientInformationByNameOrId/id/{id}")
    JSONObject selectPatientInformationByNameOrId(@PathVariable(value = "id") Integer id) {
        return inspectionApplicationRemote.selectPatientInformationByNameOrId(id);
    }

    @GetMapping("/selectPatientInformationByNameOrId/nameAndId/{name}/{id}")
    JSONObject selectPatientInformationByNameOrId(@PathVariable(value = "name") String name, @PathVariable(value = "id") Integer id) {
        return inspectionApplicationRemote.selectPatientInformationByNameOrId(name, id);
    }

    @GetMapping("/selectPatientInformationByNameOrId")
    JSONObject selectPatientInformationByNameOrId() {
        return inspectionApplicationRemote.selectPatientInformationByNameOrId();
    }


    @PostMapping("/upload")
    public JSONObject upload(@RequestParam("pic") MultipartFile pic) {
        return inspectionApplicationRemote.upload(pic);
    }
}
