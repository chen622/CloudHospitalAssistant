package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DrugTemplateMapper;
import cn.neuedu.his.model.DrugTemplate;
import cn.neuedu.his.service.DrugTemplateService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class DrugTemplateServiceImpl extends AbstractService<DrugTemplate> implements DrugTemplateService {

    @Autowired
    private DrugTemplateMapper drugTemplateMapper;


    @Override
    public List<DrugTemplate> getPrescriptionsTemByName(String name) {
        return drugTemplateMapper.getPrescriptionsTemByName(name);
    }
}
