package cn.neuedu.his.mapper;

import cn.neuedu.his.model.InspectionApplication;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component

public interface InspectionApplicationMapper extends MyMapper<InspectionApplication> {
    Integer hasMedicalRecordInspectionNotDone(Integer medicalRecordId);
    public void  deleteByTemplateId(Integer templateId);
    ArrayList<InspectionApplication> getByMedicalRecordId(Integer id);
    List<Payment> selectPatientInformationByNameOrId(String name,Integer id,Integer department_id,Boolean auth);
}