package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.MedicalRecordTemplateMapper;
import cn.neuedu.his.model.MedicalRecordTemplate;
import cn.neuedu.his.service.MedicalRecordTemplateService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.inter.AbstractService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class MedicalRecordTemplateServiceImpl extends AbstractService<MedicalRecordTemplate> implements MedicalRecordTemplateService {

    @Autowired
    private MedicalRecordTemplateMapper medicalRecordTemplateMapper;

    @Override
    @Transactional
    public List<MedicalRecordTemplate>  getHospitalMR(Integer doctorID, Integer level) {
        List<MedicalRecordTemplate> list=medicalRecordTemplateMapper.getHospitalMR(doctorID, level);
        if(list==null)
            list=new ArrayList<>();
        return list;
    }

    @Override
    @Transactional
    public List<MedicalRecordTemplate>  getDeptMR(Integer doctorID, Integer level) {
        List<MedicalRecordTemplate> list=medicalRecordTemplateMapper.getDeptMR(doctorID, level);
        if(list==null)
            list=new ArrayList<>();
        return list;
    }

    @Override
    @Transactional
    public List<MedicalRecordTemplate>  getPersonalMR(Integer doctorID, Integer level) {
        List<MedicalRecordTemplate> list=medicalRecordTemplateMapper.getPersonalMR(doctorID, level);
        if(list==null)
            list=new ArrayList<>();
        return  list;
    }

    @Override
    public List<MedicalRecordTemplate> getMeicalRecordTemByName(String name) {
        return medicalRecordTemplateMapper.getMeicalRecordTemByName(name);
    }
}
