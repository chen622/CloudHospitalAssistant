package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.DrugRemote;
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
@RequestMapping("/drug")
@Import(FeignClientsConfiguration.class)
public class DrugController {
    private DrugRemote drugRemote;

    @Autowired
    public DrugController(
            Decoder decoder, Encoder encoder, Client client) {
        this.drugRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(DrugRemote.class, "http://eureka-producer");
    }

    @PostMapping("/takeDrug/{paymentId}/{drugId}")
    public JSONObject takeDrug(@PathVariable("paymentId") Integer paymentId, @PathVariable("drugId") Integer drugId) {
       return drugRemote.takeDrug(paymentId, drugId);
    }

    @PostMapping("/retreatDrug")
    public JSONObject retreatDrug(@RequestBody JSONObject jsonObject) {
        return drugRemote.retreatDrug(jsonObject);
    }


    @PostMapping("/delete/{id}")
    public JSONObject deleteDrug(@PathVariable("id") Integer id) {
        return drugRemote.deleteDrug(id);
    }

    @PostMapping("/modify")
    public JSONObject modifyDrug(@RequestBody JSONObject jsonObject) {
        return drugRemote.modifyDrug(jsonObject);
    }

    @PostMapping("/insert")
    public JSONObject insertDrug(@RequestBody JSONObject jsonObject) {
        return drugRemote.insertDrug(jsonObject);
    }


    @GetMapping("/select/{name}")
    public JSONObject selectDrug(@PathVariable("name") String name) {
        return drugRemote.selectDrug(name);
    }

    @GetMapping("/getAllDrugType")
    public JSONObject getAllDrugType() {
        return drugRemote.getAllDrugType();
    }
    
    @GetMapping("/getAllDrug")
    public JSONObject getAllDrug() {
        return drugRemote.getAllDrug();
    }

    @GetMapping("/getAll")
    public JSONObject getAll() {
        return drugRemote.getAll();
    }
}