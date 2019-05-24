package cn.neuedu.his.controller;

import cn.neuedu.his.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/drug")
public class DrugController {

    @Autowired
    DrugService drugService;

}
