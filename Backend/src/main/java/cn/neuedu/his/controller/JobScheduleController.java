package cn.neuedu.his.controller;

import cn.neuedu.his.service.JobScheduleService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/job_schedule")
public class JobScheduleController {

    @Autowired
    JobScheduleService jobScheduleService;

    /**
     * 获取
     * @return
     */
//    @GetMapping("/getScheduleInfo")
//    public JSONObject getScheduleInfo() {
//        JSONObject result;
//        return
//    }

}
