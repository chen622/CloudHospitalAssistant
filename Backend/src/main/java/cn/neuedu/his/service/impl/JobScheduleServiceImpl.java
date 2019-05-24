package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.JobScheduleMapper;
import cn.neuedu.his.model.JobSchedule;
import cn.neuedu.his.service.JobScheduleService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class JobScheduleServiceImpl extends AbstractService<JobSchedule> implements JobScheduleService {

    @Autowired
    private JobScheduleMapper jobScheduleMapper;

}
