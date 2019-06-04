package cn.neuedu.his.service;
import cn.neuedu.his.model.InspectionApplication;
import cn.neuedu.his.util.inter.Service;

import java.util.ArrayList;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface InspectionApplicationService extends Service<InspectionApplication> {
    Boolean hasMedicalRecordInspectionNotDone(Integer medicalRecordId);
    public void  deleteByTemplateId(Integer templateId);
    ArrayList<InspectionApplication> getByMedicalRecordId(Integer id);
}
