package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.InspectionTemplateRemote;
import cn.neuedu.consumer.util.FeignRequestInterceptor;
import com.alibaba.fastjson.JSONObject;
import feign.Client;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/IT")
public class InspectionTemplateController {
    private InspectionTemplateRemote inspectionTemplateRemote;

    @Autowired
    public InspectionTemplateController(
            Decoder decoder, Encoder encoder, Client client) {
        this.inspectionTemplateRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(InspectionTemplateRemote.class, "http://eureka-producer");
    }

    @GetMapping("/getIT")
    public JSONObject getIT() {
        return inspectionTemplateRemote.getIT();
    }

    @PostMapping("/saveIT")
    public JSONObject saveInspectionTem(@RequestBody JSONObject object) {
        return inspectionTemplateRemote.saveInspectionTem(object);
    }

    @GetMapping("/getInspectionTemByName/{name}")
    public JSONObject getInspectionTemByName(@PathVariable("name") String name) {
        return inspectionTemplateRemote.getInspectionTemByName(name);
    }


    @PostMapping("/updateInspectionTem")
    public JSONObject updateInspectionTem(@RequestBody JSONObject object) {
        return inspectionTemplateRemote.updateInspectionTem(object);
    }

    @GetMapping("/deleteIT/{id}")
    public JSONObject deleteInspectionTemp(@PathVariable("id") Integer id) {
        return inspectionTemplateRemote.deleteInspectionTemp(id);
    }

}
