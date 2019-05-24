package cn.neuedu.his.controller;

import cn.neuedu.his.service.MedicalRecordTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/medical_record_template")
public class MedicalRecordTemplateController {

    @Autowired
    MedicalRecordTemplateService medicalRecordTemplateService;

}
