package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ScheduleRuleRemote {
    @PostMapping("/schedule_rule/use")
    JSONObject useRule(@RequestBody JSONObject requestBody);

    @GetMapping("/schedule_rule/getByDepartmentId/{departmentId}")
    JSONObject getByDepartmentId(@PathVariable("departmentId") Integer departmentId);

    @PostMapping("/schedule_rule/delete/{id}")
    JSONObject deleteScheduleRule(@PathVariable("id") Integer id);

    @PostMapping("/schedule_rule/insert")
    JSONObject insertScheduleRule(@RequestBody JSONObject jsonObject);


    @PostMapping("/schedule_rule/modify")
    JSONObject modifyScheduleRule(@RequestBody JSONObject jsonObject);

    @GetMapping("/schedule_rule/select/{doctorId}")
    JSONObject selectScheduleRule(@PathVariable("doctorId") Integer doctorId);
}
