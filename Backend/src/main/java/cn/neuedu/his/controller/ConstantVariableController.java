package cn.neuedu.his.controller;

import cn.neuedu.his.service.ConstantVariableService;
import cn.neuedu.his.util.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.ConditionalOnRepositoryType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/constant_variable")
public class ConstantVariableController {

    @Autowired
    ConstantVariableService constantVariableService;

    @GetMapping("/get")
    public JSONObject get(Authentication authentication) {
        System.out.println(authentication);
        return CommonUtil.successJson(constantVariableService.findAll());
    }
}
