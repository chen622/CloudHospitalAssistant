package cn.neuedu.his.mapper;

import cn.neuedu.his.model.JobSchedule;
import cn.neuedu.his.util.inter.MyMapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

@Component

public interface JobScheduleMapper extends MyMapper<JobSchedule> {
    void updateHaveRegistrationAmount(Integer id);
}