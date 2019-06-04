package cn.neuedu.his.controller;

import cn.neuedu.his.service.DailySettleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * Created by ccm on 2019/06/04.
 */
@RestController
@RequestMapping("/daily_settle")
public class DailySettleController {

    @Autowired
    DailySettleService dailySettleService;

}
