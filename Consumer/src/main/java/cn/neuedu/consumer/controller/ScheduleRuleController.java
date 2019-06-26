package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.ScheduleRuleRemote;
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
@RequestMapping("/schedule_rule")
@Import(FeignClientsConfiguration.class)
public class ScheduleRuleController {
    private ScheduleRuleRemote scheduleRuleRemote;

    @Autowired
    public ScheduleRuleController(
            Decoder decoder, Encoder encoder, Client client) {
        this.scheduleRuleRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(ScheduleRuleRemote.class, "http://eureka-producer");
    }

    @PostMapping("/use")
    public JSONObject useRule(@RequestBody JSONObject requestBody) {
        return scheduleRuleRemote.useRule(requestBody);
    }

    @GetMapping("/getByDepartmentId/{departmentId}")
    public JSONObject getByDepartmentId(@PathVariable("departmentId") Integer departmentId) {
        return scheduleRuleRemote.getByDepartmentId(departmentId);
    }

    @PostMapping("/delete/{id}")
    public JSONObject deleteScheduleRule(@PathVariable("id") Integer id) {
        return scheduleRuleRemote.deleteScheduleRule(id);
    }

    @PostMapping("/insert")
    public JSONObject insertScheduleRule(@RequestBody JSONObject jsonObject) {
        return scheduleRuleRemote.insertScheduleRule(jsonObject);
    }


    @PostMapping("/modify")
    public JSONObject modifyScheduleRule(@RequestBody JSONObject jsonObject) {
        return scheduleRuleRemote.modifyScheduleRule(jsonObject);
    }

    @GetMapping("/select/{doctorId}")
    public JSONObject selectScheduleRule(@PathVariable("doctorId") Integer doctorId) {
        return scheduleRuleRemote.selectScheduleRule(doctorId);
    }
}
