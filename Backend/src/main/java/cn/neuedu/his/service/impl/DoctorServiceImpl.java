package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DoctorMapper;
import cn.neuedu.his.model.*;
import cn.neuedu.his.service.*;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.constants.Constants;
import cn.neuedu.his.util.constants.ErrorEnum;
import cn.neuedu.his.util.inter.AbstractService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class DoctorServiceImpl extends AbstractService<Doctor> implements DoctorService {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    RegistrationService registrationService;
    @Autowired
    MedicalRecordService medicalRecordService;
    @Autowired
    InspectionTemplateService inspectionTemplateService;
    @Autowired
    InspectionTemplateRelationshipService relationshipService;
    @Autowired
    DrugTemplateService drugTemplateService;

    /**
     * 提交初诊信息
     * @param registrationID
     * @param medicalRecord
     * @return
     */
    @Override
    @Transactional
    public JSONObject setFirstDiagnose(Integer registrationID, MedicalRecord medicalRecord) {
        Registration registration = registrationService.findById(registrationID);
        if(registration==null){
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("registrationId"));
        } else{
            registration.setState(Constants.FIRST_DIAGNOSIS);
            registrationService.update(registration);
        }
        //检查是否有必要的参数没有填写完
        String  check=cheakMedicalRecord(medicalRecord);
        if(!check.equals("")){
            throw new RuntimeException();
        }
        medicalRecordService.save(medicalRecord);
        return CommonUtil.successJson();
    }

    /**
     * 获得全院模板
     * @param doctorID
     * @param level
     * @return
     */
    @Override
    public JSONObject getHospitalCheckTemps(Integer doctorID,Integer level) {
        List<InspectionTemplate> templates=inspectionTemplateService.getHospitalCheckTemps(doctorID,level,Constants.NON_DRUG);
        if(templates==null)
            templates=new ArrayList<>();
        return CommonUtil.successJson(templates);
    }

    /**
     * 获得科室模板
     * @param doctorID
     * @param level
     * @return
     */
    @Override
    public JSONObject getDeptCheckTemps(Integer doctorID,Integer level) {
//        Registration registration = registrationService.findById(registrationId);
//        if(!registration.getState().equals(Constants.FIRST_DIAGNOSIS)){
//            return  CommonUtil.errorJson(ErrorEnum.E_601.addErrorParamName("registration state"));
//        }
        List<InspectionTemplate> templates=inspectionTemplateService.getDeptCheckTemps(doctorID,level,Constants.NON_DRUG);
        if(templates==null)
            templates=new ArrayList<>();
        return CommonUtil.successJson(templates);
    }

    /**
     * 获得个人模板
     * @param doctorID
     * @param level
     * @return
     */
    @Override
    public JSONObject getPersonalCheckTemps(Integer doctorID,Integer level) {
        List<InspectionTemplate> templates=inspectionTemplateService.getPersonalCheckTemps(doctorID,level,Constants.NON_DRUG);
        if(templates==null)
            templates=new ArrayList<>();
        return CommonUtil.successJson(templates);
    }

    private String cheakMedicalRecord(MedicalRecord record){
        if (registrationService.findById(record.getRegistrationId())==null)
            return "RegistrationId";
        if (record.getSelfDescription()==null || record.getSelfDescription().equals("")  )
            return "SelfDescription";
        if (record.getCurrentSymptom()==null || record.getCurrentSymptom().equals(""))
            return "CurrentSymptom";
        if (record.getIsWesternMedicine()==null )
            return  "isWesternMedicne";
        return "";
    }
}
