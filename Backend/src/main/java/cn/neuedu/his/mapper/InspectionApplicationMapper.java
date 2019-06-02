package cn.neuedu.his.mapper;

import cn.neuedu.his.model.InspectionApplication;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

@Component

public interface InspectionApplicationMapper extends MyMapper<InspectionApplication> {
    Integer hasMedicalRecordInspectionNotDone(Integer medicalRecordId);
}