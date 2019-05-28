package cn.neuedu.his.controller;

import cn.neuedu.his.model.RegistrationType;
import cn.neuedu.his.service.RegistrationTypeService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public JSONObject insertRegisterType(JSONObject jsonObject, Authentication authentication){
        //检查权限
        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        RegistrationType registrationType = jsonObject.toJavaObject(jsonObject,RegistrationType.class);
        //判断挂号类型是否已经存在
        if (registrationTypeService.getRegistrationTypeByName(registrationType.getName())!= null)
            return CommonUtil.errorJson(ErrorEnum.E_603);

        registrationTypeService.save(registrationType);

        return CommonUtil.successJson(registrationType);
    }

    /**
     * 医院管理员删除挂号类型
     * @param registerName
     * @param authentication
     * @return
     */
    @PostMapping("/deleteRegisterType")
    public JSONObject deleteRegisterType(String registerName,Authentication authentication){
        //检查权限
        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        //判断挂号类型是否存在
        if (registrationTypeService.getRegistrationTypeByName(registerName)== null)
            return CommonUtil.errorJson(ErrorEnum.E_604);

        registrationTypeService.deleteRegistrationTypeByName(registerName);

        return CommonUtil.successJson(registerName);
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
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        RegistrationType registrationType = jsonObject.toJavaObject(jsonObject,RegistrationType.class);
        //判断挂号类型是否存在
        if (registrationTypeService.getRegistrationTypeByName(registrationType.getName())== null)
            return CommonUtil.errorJson(ErrorEnum.E_604);

        registrationTypeService.save(registrationType);
        return CommonUtil.successJson(registrationType);
    }

    /**
     * 医院管理员查询数据
     * @param
     * @param authentication
     * @return
     */
    @PostMapping("/selectRegisterType")
    public JSONObject selectRegisterType(String name, Authentication authentication){
        //检查权限
        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        RegistrationType registrationType = registrationTypeService.getRegistrationTypeByName(name);
        //判断挂号类型是否存在
        if (registrationType== null)
            return CommonUtil.errorJson(ErrorEnum.E_604);

        return CommonUtil.successJson(registrationType);
    }
}
