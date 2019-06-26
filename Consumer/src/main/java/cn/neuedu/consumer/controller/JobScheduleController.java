package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.JobScheduleRemote;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job_schedule")
@Import(FeignClientsConfiguration.class)
public class JobScheduleController {
    private JobScheduleRemote jobScheduleRemote;

    @Autowired
    public JobScheduleController(
            Decoder decoder, Encoder encoder, Client client) {
        this.jobScheduleRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(JobScheduleRemote.class, "http://eureka-producer");
    }

    @GetMapping("/getSchedule/{departmentId}")
    public JSONObject getSchedule(@PathVariable("departmentId") Integer departmentId) {
        return jobScheduleRemote.getSchedule(departmentId);
    }

    @GetMapping("/getPreSchedule/{departmentId}")
    public JSONObject getPreSchedule(@PathVariable("departmentId") Integer departmentId) {
        return jobScheduleRemote.getPreSchedule(departmentId);
    }

    @GetMapping("/getScheduleWithMonth/{departmentId}/{date}")
    public JSONObject getScheduleByMonth(@PathVariable("departmentId") Integer departmentId, @PathVariable("date") String date) {
        return jobScheduleRemote.getScheduleByMonth(departmentId, date);
    }

    @GetMapping("/getScheduleAndLastAndNextWithMonth/{departmentId}/{date}")
    public JSONObject getScheduleAndLastAndNextByMonth(@PathVariable("departmentId") Integer departmentId, @PathVariable("date") String date) {
        return jobScheduleRemote.getScheduleAndLastAndNextByMonth(departmentId, date);
    }
}
