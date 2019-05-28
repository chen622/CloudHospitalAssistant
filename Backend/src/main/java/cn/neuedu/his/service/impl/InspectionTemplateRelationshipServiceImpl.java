package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.InspectionTemplateRelationshipMapper;
import cn.neuedu.his.model.InspectionTemplateRelationship;
import cn.neuedu.his.service.InspectionTemplateRelationshipService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class InspectionTemplateRelationshipServiceImpl extends AbstractService<InspectionTemplateRelationship> implements InspectionTemplateRelationshipService {

    @Autowired
    private InspectionTemplateRelationshipMapper inspectionTemplateRelationshipMapper;

    @Override
    public List<InspectionTemplateRelationship> getItem(Integer templateID, Integer nonDrugType) {
        return inspectionTemplateRelationshipMapper.getItem(templateID, nonDrugType);
    }
}
