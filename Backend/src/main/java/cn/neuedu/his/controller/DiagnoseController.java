package cn.neuedu.his.controller;

import cn.neuedu.his.model.Diagnose;
import cn.neuedu.his.service.DiagnoseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/diagnose")
public class DiagnoseController {

    @Autowired
    DiagnoseService diagnoseService;

}
