package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.MedicalRecordTemplateMapper;
import cn.neuedu.his.model.MedicalRecordTemplate;
import cn.neuedu.his.service.MedicalRecordTemplateService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class MedicalRecordTemplateServiceImpl extends AbstractService<MedicalRecordTemplate> implements MedicalRecordTemplateService {

    @Autowired
    private MedicalRecordTemplateMapper medicalRecordTemplateMapper;

}
