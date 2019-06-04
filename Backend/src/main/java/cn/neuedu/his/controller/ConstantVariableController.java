package cn.neuedu.his.controller;

import cn.neuedu.his.model.ConstantVariable;
import cn.neuedu.his.service.ConstantVariableService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.ConditionalOnRepositoryType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
        Integer userId = PermissionCheck.getIdByUser(authentication);
        return CommonUtil.successJson(constantVariableService.findAll());
    }

    @PostMapping("/getName")
    public  JSONObject getNamebyId(Integer id){
        return CommonUtil.successJson(constantVariableService.findById(id).getName());
    }

    /**
     * 增加常量小类(type 及以后)
     * @param jsonObject
     * @param authentication
     * @return
     */
    @PostMapping("/insert")
    public JSONObject insertConstant(@RequestBody JSONObject jsonObject, Authentication authentication){
        try{
            PermissionCheck.isHosptialAdim(authentication);
            ConstantVariable constantVariable = jsonObject.toJavaObject(jsonObject,ConstantVariable.class);
            constantVariableService.insertConstant(constantVariable);
            return CommonUtil.successJson();
        }catch (RuntimeException e){
            if (e.getMessage().equals("629"))
                return CommonUtil.errorJson(ErrorEnum.E_629);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }


    @PostMapping("/delete")
    public JSONObject deleteConstant(@PathVariable("id") Integer id, Authentication authentication){
        try{
            PermissionCheck.isHosptialAdim(authentication);
            constantVariableService.deleteConstant(id);
            return CommonUtil.successJson();
        }catch (RuntimeException e){
            if (e.getMessage().equals("629"))
                return CommonUtil.errorJson(ErrorEnum.E_629);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }
}
