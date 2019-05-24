package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.NonDrugMapper;
import cn.neuedu.his.model.NonDrug;
import cn.neuedu.his.service.NonDrugService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class NonDrugServiceImpl extends AbstractService<NonDrug> implements NonDrugService {

    @Autowired
    private NonDrugMapper nonDrugMapper;

}
