package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.InspectionApplicationMapper;
import cn.neuedu.his.model.InspectionApplication;
import cn.neuedu.his.service.InspectionApplicationService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class InspectionApplicationServiceImpl extends AbstractService<InspectionApplication> implements InspectionApplicationService {

    @Autowired
    private InspectionApplicationMapper inspectionApplicationMapper;

    @Override
    public Boolean hasMedicalRecordInspectionNotDone(Integer medicalRecordId) {
        return inspectionApplicationMapper.hasMedicalRecordInspectionNotDone(medicalRecordId)>0?true:false;
    }
}
