package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface JobScheduleRemote {
    @GetMapping("/job_schedule/getSchedule/{departmentId}")
    JSONObject getSchedule(@PathVariable("departmentId") Integer departmentId);

    @GetMapping("/job_schedule/getPreSchedule/{departmentId}")
    JSONObject getPreSchedule(@PathVariable("departmentId") Integer departmentId);

    @GetMapping("/job_schedule/getScheduleWithMonth/{departmentId}/{date}")
    JSONObject getScheduleByMonth(@PathVariable("departmentId") Integer departmentId, @PathVariable("date") String date);

    @GetMapping("/job_schedule/getScheduleAndLastAndNextWithMonth/{departmentId}/{date}")
    JSONObject getScheduleAndLastAndNextByMonth(@PathVariable("departmentId") Integer departmentId, @PathVariable("date") String date);
}
