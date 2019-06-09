package cn.neuedu.his.controller;

import cn.neuedu.his.model.MedicalRecord;
import cn.neuedu.his.service.PrescriptionService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * Created by ccm on 2019/05/29.
 */
@RestController
@RequestMapping("/prescription")
public class PrescriptionController {

    @Autowired
    PrescriptionService prescriptionService;

}
