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
    public List<InspectionTemplate> getHospitalCheckTemps(Integer doctorID,Integer level) {
        return inspectionTemplateMapper.getHospitalCheckTemps(doctorID,level);
    }

    @Override
    public List<InspectionTemplate> getDeptCheckTemps(Integer doctorID, Integer level) {
        return inspectionTemplateMapper.getDeptCheckTemps(doctorID, level);
    }

    @Override
    public List<InspectionTemplate> getPersonalCheckTemps(Integer doctorID, Integer level) {
        return inspectionTemplateMapper.getPersonalCheckTemps(doctorID, level);
    }

    @Override
    public void deleteRelationship(Integer id){
        inspectionTemplateMapper.deleteRelationship(id);
    }

    @Override
    public List<InspectionTemplate> getInspectionTemByName(String name) {
        return inspectionTemplateMapper.getInspectionTemByName(name);
    }
}
