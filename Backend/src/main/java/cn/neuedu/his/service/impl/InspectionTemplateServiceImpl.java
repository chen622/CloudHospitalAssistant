package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.InspectionTemplateMapper;
import cn.neuedu.his.model.DrugTemplate;
import cn.neuedu.his.model.InspectionTemplate;
import cn.neuedu.his.service.InspectionTemplateRelationshipService;
import cn.neuedu.his.service.InspectionTemplateService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class InspectionTemplateServiceImpl extends AbstractService<InspectionTemplate> implements InspectionTemplateService {

    @Autowired
    private InspectionTemplateMapper inspectionTemplateMapper;

    @Autowired
    private InspectionTemplateRelationshipService relationshipService;

    @Override
    public List<InspectionTemplate> getHospitalCheckTemps(Integer doctorID,Integer level,Integer nonDrugType) {
        return inspectionTemplateMapper.getHospitalCheckTemps(doctorID,level,nonDrugType);
    }

    @Override
    public List<InspectionTemplate> getDeptCheckTemps(Integer doctorID, Integer level, Integer nonDrugType) {
        return inspectionTemplateMapper.getDeptCheckTemps(doctorID, level,nonDrugType);
    }

    @Override
    public List<InspectionTemplate> getPersonalCheckTemps(Integer doctorID, Integer level, Integer nonDrugType) {
        return inspectionTemplateMapper.getPersonalCheckTemps(doctorID, level,nonDrugType);
    }

}
