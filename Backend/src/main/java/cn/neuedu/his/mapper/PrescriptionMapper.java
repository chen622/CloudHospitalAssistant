package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Prescription;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public interface PrescriptionMapper extends MyMapper<Prescription> {
    void deleteByTemplateId(Integer templateId);
    ArrayList<Prescription> getByMedicalRecordId(Integer id);
    Prescription getPrescriptionAndDrug(Integer id);
    ArrayList<Prescription> getPrescriptionAndDrugByMedical(Integer medicalId);
}