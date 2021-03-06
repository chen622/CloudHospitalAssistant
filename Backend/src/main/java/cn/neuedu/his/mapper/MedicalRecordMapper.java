package cn.neuedu.his.mapper;

import cn.neuedu.his.model.MedicalRecord;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public interface MedicalRecordMapper extends MyMapper<MedicalRecord> {
     List<MedicalRecord> getAllByPatientId(Integer patient_id);
     MedicalRecord getMedicalRecordWithDiagnose(Integer id);
     MedicalRecord getByRegistrationId(Integer registrationId);
     List<MedicalRecord> getAllByPatientIdTwo(Integer patient_id);
     MedicalRecord getDrugPrescription(Integer medicalId);
     MedicalRecord getApplicationAndResultByMedicalId(Integer medicalId);
     MedicalRecord getApplicationAndNonDrugByMedicalId(Integer medicalId);
     MedicalRecord getPrescriptionAndPayment(Integer medicalId);
}