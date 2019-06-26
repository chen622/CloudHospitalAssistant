package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.DiseaseFirstRemote;
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
@RequestMapping("/disease_first")
@Import(FeignClientsConfiguration.class)
public class DiseaseFirstController {
    private DiseaseFirstRemote diseaseFirstRemote;

    @Autowired
    public DiseaseFirstController(
            Decoder decoder, Encoder encoder, Client client) {
        this.diseaseFirstRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(DiseaseFirstRemote.class, "http://eureka-producer");
    }

    @PostMapping("/delete/{id}")
    public JSONObject deleteDiseaseFirst(@PathVariable Integer id){
        return diseaseFirstRemote.deleteDiseaseFirst(id);
    }

    @PostMapping("/insert")
    public JSONObject insertDiseaseFirst(@RequestBody JSONObject jsonObject){
        return diseaseFirstRemote.insertDiseaseFirst(jsonObject);
    }

    @PostMapping("/modify")
    public JSONObject modifyDiseaseFirst(@RequestBody JSONObject jsonObject){
        return diseaseFirstRemote.modifyDiseaseFirst(jsonObject);
    }

    @PostMapping("/select/{name}")
    public JSONObject selectDiseaseFirst(@PathVariable("name") String name){
        return diseaseFirstRemote.selectDiseaseFirst(name);
    }

    @GetMapping("/getType")
    public JSONObject getAllDiseaseType(){
        return diseaseFirstRemote.getAllDiseaseType();
    }
}
