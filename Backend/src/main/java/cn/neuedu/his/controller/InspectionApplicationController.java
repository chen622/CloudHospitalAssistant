package cn.neuedu.his.controller;

import cn.neuedu.his.service.InspectionApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/inspection_application")
public class InspectionApplicationController {

    @Autowired
    InspectionApplicationService inspectionApplicationService;

}
