package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.NonDrugRemote;
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

import java.util.List;

@RestController
@RequestMapping("/non_drug")
@Import(FeignClientsConfiguration.class)
public class NonDrugController {
    private NonDrugRemote nonDrugRemote;

    @Autowired
    public NonDrugController(
            Decoder decoder, Encoder encoder, Client client) {
        this.nonDrugRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(NonDrugRemote.class, "http://eureka-producer");
    }

    @PostMapping("/insert")
    public JSONObject insertNonDrug(@RequestBody JSONObject jsonObject){
        return nonDrugRemote.insertNonDrug(jsonObject);
    }

   
    @GetMapping("/selectByName/{name}")
    public JSONObject selectNonDrugByName(@PathVariable("name") String name){
        return nonDrugRemote.selectNonDrugByName(name);
    }

    @GetMapping("/selectByCode/{code}")
    public JSONObject selectNonDrugByCode(@PathVariable("code") String code){
        return nonDrugRemote.selectNonDrugByCode(code);
    }

    @PostMapping("/delete/{id}")
    public JSONObject deleteNonDrug(@PathVariable("id") Integer id){
        return nonDrugRemote.deleteNonDrug(id);
    }

    @PostMapping("/modify")
    public JSONObject modifyNonDrug(@RequestBody JSONObject jsonObject){
        return nonDrugRemote.modifyNonDrug(jsonObject);
    }

 
    @GetMapping("/getTypeAndNonDrug/")
    JSONObject getTypeAndNonDrug(){
        return nonDrugRemote.getTypeAndNonDrug();
    }
}
