package cn.neuedu.his.controller;

import cn.neuedu.his.model.RegistrationType;
import cn.neuedu.his.service.RegistrationTypeService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/registration_type")
public class RegistrationTypeController {

    @Autowired
    RegistrationTypeService registrationTypeService;

    /**
     * 医院管理员插入挂号类型
     * @param jsonObject
     * @param authentication
     * @return
     */
    @PostMapping("/insertRegisterType")
    public JSONObject insertRegisterType(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHospitalAdmin(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        RegistrationType registrationType = JSONObject.toJavaObject(jsonObject,RegistrationType.class);

       try{
           registrationTypeService.insertRegisterType(registrationType);
           return CommonUtil.successJson();
       }catch (RuntimeException e){
           if (e.getMessage().equals("603"))
               return CommonUtil.errorJson(ErrorEnum.E_603);
           else
               return CommonUtil.errorJson(ErrorEnum.E_500);
       }
    }

    /**
     * 医院管理员删除挂号类型
     * @param id
     * @param authentication
     * @return
     */
    @PostMapping("/deleteRegisterType/{id}")
    public JSONObject deleteRegisterType(@PathVariable("id") Integer id, Authentication authentication){
        //检查权限
        try {
            PermissionCheck.isHospitalAdmin(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

       try {
           registrationTypeService.deleteRegisterType(id);
           return CommonUtil.successJson();
       }catch (RuntimeException e){
           if (e.getMessage().equals("604"))
               return CommonUtil.errorJson(ErrorEnum.E_604);
           else
               return CommonUtil.errorJson(ErrorEnum.E_500);
       }
    }

    /**
     * 医院管理员修改挂号类型
     * @param jsonObject
     * @param authentication
     * @return
     */
    @PostMapping("/modifyRegisterType")
    public JSONObject modifyRegisterType(@RequestBody JSONObject jsonObject, Authentication authentication){
        //检查权限
        try {
            PermissionCheck.isHospitalAdmin(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        RegistrationType registrationType = JSONObject.toJavaObject(jsonObject,RegistrationType.class);

        try{
            registrationTypeService.modifyRegisterType(registrationType);
            return CommonUtil.successJson();
        }catch (RuntimeException e){
            if (e.getMessage().equals("604"))
                return CommonUtil.errorJson(ErrorEnum.E_604);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }

    /**
     * 医院管理员查询数据
     * @param
     * @param authentication
     * @return
     */
    @GetMapping("/selectRegisterType/{name}")
    public JSONObject selectRegisterType(@PathVariable("name") String name, Authentication authentication){
        //检查权限
        try {
            PermissionCheck.isHospitalAdmin(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        try{
            RegistrationType registrationType = registrationTypeService.selectRegisterType(name);
            return CommonUtil.successJson(registrationType);
        }catch (RuntimeException e){
            if (e.getMessage().equals("604"))
                return CommonUtil.errorJson(ErrorEnum.E_604);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }
}
