package cn.neuedu.his.service;
import cn.neuedu.his.model.MedicalRecord;
import cn.neuedu.his.model.MedicalRecordTemplate;
import cn.neuedu.his.util.inter.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface MedicalRecordService extends Service<MedicalRecord> {
     @Transactional
     List<MedicalRecord> getAllByPatientId(Integer patient_id);
     @Transactional
     List<MedicalRecord> getAllByPatientIdTwo(Integer patient_id);
     @Transactional
     MedicalRecord getMedicalRecordWithDiagnose(Integer id);
     @Transactional
     MedicalRecord getByRegistrationId(Integer registrationid);

     MedicalRecord getDrugNonDrugAndResultByMedicalId(Integer id);
}
