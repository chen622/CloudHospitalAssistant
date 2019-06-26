package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.DepartmentRemote;
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
@RequestMapping("/department")
@Import(FeignClientsConfiguration.class)
public class DepartmentController {
    private DepartmentRemote departmentRemote;

    @Autowired
    public DepartmentController(
            Decoder decoder, Encoder encoder, Client client) {
        this.departmentRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(DepartmentRemote.class, "http://eureka-producer");
    }

    @GetMapping("/getByKind/{kindId}")
    public JSONObject getByKind(@PathVariable("kindId") Integer kindId) {
        return departmentRemote.getByKind(kindId);
    }

    @GetMapping("/get")
    public JSONObject getDepartmentInformation() {
        return departmentRemote.getDepartmentInformation();
    }

    @PostMapping("/delete/{id}")
    public JSONObject deleteDepartmentInformation(@PathVariable("id") Integer id) {
        return departmentRemote.deleteDepartmentInformation(id);
    }

    @PostMapping("/add")
    public JSONObject addDepartment(@RequestBody JSONObject jsonObject) {
        return departmentRemote.addDepartment(jsonObject);
    }

    @PostMapping("/modify")
    public JSONObject modifyDepartment(@RequestBody JSONObject jsonObject) {
        return departmentRemote.modifyDepartment(jsonObject);
    }

    @PostMapping("/retreat/{id}")
    public JSONObject modifyDepartment(@PathVariable("id") Integer id) {
        return departmentRemote.modifyDepartment(id);
    }

    @GetMapping("/getAllDepartmentKind")
    public JSONObject getAllDepartmentMatchKind() {
        return departmentRemote.getAllDepartmentMatchKind();
    }

    @GetMapping("/getDepartmentList/{name}")
    public JSONObject getDepartmentListByname(@PathVariable(name = "name") String name) {
        return departmentRemote.getDepartmentListByname(name);
    }

    @GetMapping("/getDepartmentList/")
    public JSONObject getDepartmentListByname() {
        return departmentRemote.getDepartmentListByname();
    }

    @PostMapping("/departmentClinicWorkload")
    public JSONObject getClinicDepartmentWorkLoad(@RequestBody JSONObject jsonObject) {
        return departmentRemote.getClinicDepartmentWorkLoad(jsonObject);
    }

    @PostMapping("/departmentTechniqueWorkload")
    public JSONObject getTechniqueDepartmentWorkLoad(@RequestBody JSONObject jsonObject) {
        return departmentRemote.getTechniqueDepartmentWorkLoad(jsonObject);
    }

}
