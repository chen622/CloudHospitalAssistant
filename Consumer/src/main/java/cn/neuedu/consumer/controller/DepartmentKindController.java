package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.DepartmentKindRemote;
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
import java.util.Map;

@RestController
@RequestMapping("/department_kind")
@Import(FeignClientsConfiguration.class)
public class DepartmentKindController {
    private DepartmentKindRemote departmentKindRemote;

    @Autowired
    public DepartmentKindController(
            Decoder decoder, Encoder encoder, Client client) {
        this.departmentKindRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(DepartmentKindRemote.class, "http://eureka-producer");
    }

    @PostMapping("/add")
    public JSONObject addDepartmentKind(@RequestBody JSONObject jsonObject){
        return departmentKindRemote.addDepartmentKind(jsonObject);
    }

    @PostMapping("/modify")
    public JSONObject modifyDepartmentKinds(@RequestBody JSONObject jsonObject){
        return modifyDepartmentKinds(jsonObject);
    }

    @PostMapping("/delete/{id}")
    public JSONObject deleteDepartmentKinds(@PathVariable("id") Integer id){
        return deleteDepartmentKinds(id);
    }

    @GetMapping("/getAll")
    public JSONObject getDepartmentKindList(){
        return getDepartmentKindList();
    }

    @GetMapping("/getDepartmentKindAndDepartment")
    public JSONObject getDepartmentKindAndDepartment(){
        return getDepartmentKindAndDepartment();
    }

    @GetMapping("/getClinical")
    public JSONObject getClinical(){
        return getClinical();
    }

}
