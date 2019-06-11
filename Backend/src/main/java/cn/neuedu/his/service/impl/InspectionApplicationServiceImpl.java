package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.InspectionApplicationMapper;
import cn.neuedu.his.model.InspectionApplication;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.service.InspectionApplicationService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class InspectionApplicationServiceImpl extends AbstractService<InspectionApplication> implements InspectionApplicationService {

    @Autowired
    private InspectionApplicationMapper inspectionApplicationMapper;

    @Override
    public Boolean hasMedicalRecordInspectionNotDone(Integer medicalRecordId) {
        return inspectionApplicationMapper.hasMedicalRecordInspectionNotDone(medicalRecordId)>0?true:false;
    }

    @Override
    public void deleteByTemplateId(Integer templateId) {
        inspectionApplicationMapper.deleteByTemplateId(templateId);
    }

    @Override
    public ArrayList<InspectionApplication> getByMedicalRecordId(Integer id) {
        return inspectionApplicationMapper.getByMedicalRecordId(id);
    }

    @Override
    public List<Payment> selectPatientInformationByNameOrId(String name, Integer id, Integer department_id, Boolean auth) {
        return inspectionApplicationMapper.selectPatientInformationByNameOrId(name,id,department_id,auth);
    }

    @Override
    public InspectionApplication findInspectionAndNonDrug(Integer id) {
        return inspectionApplicationMapper.getInspectionAndNonDrug(id);
    }

    @Override
    public ArrayList<InspectionApplication> findAllByMedical(Integer medicalId) {
        return inspectionApplicationMapper.getAllByMedical(medicalId);
    }
}
