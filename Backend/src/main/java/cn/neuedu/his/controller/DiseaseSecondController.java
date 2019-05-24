package cn.neuedu.his.controller;

import cn.neuedu.his.service.DiseaseSecondService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/disease_second")
public class DiseaseSecondController {

    @Autowired
    DiseaseSecondService diseaseSecondService;

}
