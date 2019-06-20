package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.MedicalRecordTemplateMapper;
import cn.neuedu.his.model.MedicalRecordTemplate;
import cn.neuedu.his.service.MedicalRecordTemplateService;
import cn.neuedu.his.service.UserService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ccm on 2019/05/24.
 */
@Service
public class MedicalRecordTemplateServiceImpl extends AbstractService<MedicalRecordTemplate> implements MedicalRecordTemplateService {

    @Autowired
    MedicalRecordTemplateMapper medicalRecordTemplateMapper;


    @Override
    public List<MedicalRecordTemplate> getHospitalMRByLevel(Integer doctorID, Integer level) {
        List<MedicalRecordTemplate> list = medicalRecordTemplateMapper.getHospitalMRByLevel(doctorID, level);
        if (list == null)
            list = new ArrayList<>();
        return list;
    }

    @Override
    public List<MedicalRecordTemplate> getWhichICanUse(Integer doctorId,Integer departmentId) {
        List<MedicalRecordTemplate> list = medicalRecordTemplateMapper.getHospitalMRWhichICanUse(doctorId, departmentId);
        if (list == null)
            list = new ArrayList<>();
        return list;
    }

    @Override
    public List<MedicalRecordTemplate> getDeptMRByLevel(Integer doctorID, Integer level) {
        List<MedicalRecordTemplate> list = medicalRecordTemplateMapper.getDeptMR(doctorID, level);
        if (list == null)
            list = new ArrayList<>();
        return list;
    }

    @Override
    public List<MedicalRecordTemplate> getPersonalMR(Integer doctorID, Integer level) {
        List<MedicalRecordTemplate> list = medicalRecordTemplateMapper.getPersonalMR(doctorID, level);
        if (list == null)
            list = new ArrayList<>();
        return list;
    }

    @Override
    public List<MedicalRecordTemplate> getMedicalRecordTemByName(String name) {
        return medicalRecordTemplateMapper.getMeicalRecordTemByName(name);
    }
}
