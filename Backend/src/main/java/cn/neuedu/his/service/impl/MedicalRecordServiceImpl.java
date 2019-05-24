package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.MedicalRecordMapper;
import cn.neuedu.his.model.MedicalRecord;
import cn.neuedu.his.service.MedicalRecordService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class MedicalRecordServiceImpl extends AbstractService<MedicalRecord> implements MedicalRecordService {

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

}
