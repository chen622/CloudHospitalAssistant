package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.ScheduleRuleMapper;
import cn.neuedu.his.model.ScheduleRule;
import cn.neuedu.his.service.ScheduleRuleService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class ScheduleRuleServiceImpl extends AbstractService<ScheduleRule> implements ScheduleRuleService {

    @Autowired
    private ScheduleRuleMapper scheduleRuleMapper;

}
