package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.FrequentlyUsedMapper;
import cn.neuedu.his.model.FrequentlyUsed;
import cn.neuedu.his.service.FrequentlyUsedService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class FrequentlyUsedServiceImpl extends AbstractService<FrequentlyUsed> implements FrequentlyUsedService {

    @Autowired
    private FrequentlyUsedMapper frequentlyUsedMapper;

}
