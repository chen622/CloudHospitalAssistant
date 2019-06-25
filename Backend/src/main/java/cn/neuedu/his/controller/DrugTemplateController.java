package cn.neuedu.his.controller;

import cn.neuedu.his.model.DrugTemplate;
import cn.neuedu.his.service.DoctorService;
import cn.neuedu.his.service.DrugTemplateService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/drug_template")
public class DrugTemplateController {

    @Autowired
    DrugTemplateService drugTemplateService;
    @Autowired
    DoctorService doctorService;


    @PostMapping("/updateTem")
    public JSONObject updateTem(@RequestBody JSONObject object, Authentication authentication){
        try{
            Integer doctorId= PermissionCheck.isOutpatientDoctor(authentication);
            DrugTemplate template=JSONObject.parseObject(object.get("template").toString(), DrugTemplate.class);
            doctorService.updatePrescriptionsTemp(template,doctorId);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
        return CommonUtil.successJson();
    }

    @GetMapping("/deleteTem/{id}")
    public JSONObject deleteTem(@PathVariable("id") Integer id, Authentication authentication){
        try{
            PermissionCheck.isOutpatientDoctor(authentication);
            return drugTemplateService.deleteTem(id);
        }catch (AuthenticationServiceException a){
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
    }
}
