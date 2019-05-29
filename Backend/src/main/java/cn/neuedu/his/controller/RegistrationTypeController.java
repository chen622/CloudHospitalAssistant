package cn.neuedu.his.controller;

import cn.neuedu.his.model.RegistrationType;
import cn.neuedu.his.service.RegistrationTypeService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
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
     * @param name
     * @param authentication
     * @return
     */
    @PostMapping("/deleteRegisterType/{name}")
    public JSONObject deleteRegisterType(@PathVariable("name") String name, Authentication authentication){
        //检查权限
        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        System.out.println(name);

        //判断挂号类型是否存在
        if (registrationTypeService.getRegistrationTypeByName(name) == null)
            return CommonUtil.errorJson(ErrorEnum.E_604);

        registrationTypeService.deleteRegistrationTypeByName(name);

        return CommonUtil.successJson(name);
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
        RegistrationType beforeType = registrationTypeService.getRegistrationTypeByName(registrationType.getName());
        //判断挂号类型是否存在
        if (beforeType == null)
            return CommonUtil.errorJson(ErrorEnum.E_604);

        registrationType.setId(beforeType.getId());
        registrationTypeService.update(registrationType);
        return CommonUtil.successJson(registrationType);
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
