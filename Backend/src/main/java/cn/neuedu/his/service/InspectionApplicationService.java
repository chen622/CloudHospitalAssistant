package cn.neuedu.his.service;
import cn.neuedu.his.model.InspectionApplication;
import cn.neuedu.his.util.inter.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface InspectionApplicationService extends Service<InspectionApplication> {
    Boolean hasMedicalRecordInspection(Integer medicalRecordId);
}
