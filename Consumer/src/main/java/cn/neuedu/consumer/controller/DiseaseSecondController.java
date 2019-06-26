package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.DiseaseSecondRemote;
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
@RequestMapping("/disease_second")
@Import(FeignClientsConfiguration.class)
public class DiseaseSecondController {
    private DiseaseSecondRemote diseaseSecondRemote;

    @Autowired
    public DiseaseSecondController(
            Decoder decoder, Encoder encoder, Client client) {
        this.diseaseSecondRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(DiseaseSecondRemote.class, "http://eureka-producer");
    }

    @PostMapping("/insert")
    public JSONObject insertDiseaseSecond(@RequestBody JSONObject jsonObject){
        return diseaseSecondRemote.insertDiseaseSecond(jsonObject);
    }

    @PostMapping("/delete/{id}")
    public JSONObject deleteDiseaseSecond(@PathVariable("id") Integer id){
        return diseaseSecondRemote.deleteDiseaseSecond(id);
    }

    @GetMapping("/selectByName/{name}")
    public JSONObject selectDiseaseSecondByName(@PathVariable("name") String name){
        return diseaseSecondRemote.selectDiseaseSecondByName(name);
    }

    @GetMapping("/selectByIcd/{icdId}")
    public JSONObject selectDiseaseSecondByIcd(@PathVariable("icdId") String icdId){
        return diseaseSecondRemote.selectDiseaseSecondByIcd(icdId);
    }

    @PostMapping("/modify")
    public JSONObject modifyDiseaseSecond(@RequestBody JSONObject jsonObject){
        return diseaseSecondRemote.modifyDiseaseSecond(jsonObject);
    }

    @GetMapping({"/getDiseaseByType/{type}","/getDiseaseByType"})
    JSONObject getDiseaseByType(@PathVariable(value = "type",required = false) Integer type){
        return diseaseSecondRemote.getDiseaseByType(type);
    }
}
