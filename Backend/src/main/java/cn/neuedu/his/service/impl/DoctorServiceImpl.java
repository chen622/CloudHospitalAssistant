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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class DoctorServiceImpl extends AbstractService<Doctor> implements DoctorService {

    @Autowired
    private DoctorMapper doctorMapper;
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

    @Override
    public List<InspectionTemplate> getHospitalCheckTemps(Integer doctorID,Integer level) {
        List<InspectionTemplate> templates=inspectionTemplateService.getHospitalCheckTemps(doctorID,level,Constants.NON_DRUG);
        if(templates==null)
            templates=new ArrayList<>();
        else {
            System.out.println("\n\n**********************************");
            for (InspectionTemplate t:templates){
                System.out.println(t.getId()+" "+t.getName());
                for (InspectionTemplateRelationship relationship :t.getRelationships()){
                    System.out.println(relationship.getId()+" "+relationship.getItemId());
                    if (relationship.getNonDrug()!=null)
                        System.out.println(relationship.getNonDrug().getName());
                    else
                        System.out.println(relationship.getDrug().getName());
                }
            }
        }
        return templates;
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
