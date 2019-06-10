package cn.neuedu.his.service;
import cn.neuedu.his.model.InspectionApplication;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.util.inter.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface InspectionApplicationService extends Service<InspectionApplication> {
    Boolean hasMedicalRecordInspectionNotDone(Integer medicalRecordId);
    public void  deleteByTemplateId(Integer templateId);
    ArrayList<InspectionApplication> getByMedicalRecordId(Integer id);
    List<Payment> selectPatientInformationByNameOrId(String name, Integer id, Integer department_id,Boolean auth);
}
