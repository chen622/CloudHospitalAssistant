package cn.neuedu.his.service;
import cn.neuedu.his.model.MedicalRecord;
import cn.neuedu.his.model.MedicalRecordTemplate;
import cn.neuedu.his.util.inter.Service;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface MedicalRecordService extends Service<MedicalRecord> {
     List<MedicalRecord> getAllByPatientId(Integer patient_id);
     MedicalRecord getMedicalRecordWithDiagnose(Integer id);
}
