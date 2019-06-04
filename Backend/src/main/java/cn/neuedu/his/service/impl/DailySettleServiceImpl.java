package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DailySettleMapper;
import cn.neuedu.his.model.DailySettle;
import cn.neuedu.his.service.DailySettleService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by ccm on 2019/06/04.
 */
@Service
public class DailySettleServiceImpl extends AbstractService<DailySettle> implements DailySettleService {

    @Autowired
    private DailySettleMapper dailySettleMapper;

}
