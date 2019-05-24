package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.InspectionTemplateMapper;
import cn.neuedu.his.model.InspectionTemplate;
import cn.neuedu.his.service.InspectionTemplateService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class InspectionTemplateServiceImpl extends AbstractService<InspectionTemplate> implements InspectionTemplateService {

    @Autowired
    private InspectionTemplateMapper inspectionTemplateMapper;

}
