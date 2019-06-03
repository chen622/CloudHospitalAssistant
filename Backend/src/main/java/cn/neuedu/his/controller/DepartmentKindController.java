package cn.neuedu.his.controller;

import cn.neuedu.his.model.ConstantVariable;
import cn.neuedu.his.model.DepartmentKind;
import cn.neuedu.his.service.ConstantVariableService;
import cn.neuedu.his.service.DepartmentKindService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static cn.neuedu.his.util.constants.Constants.DEPARTMENT_KIND_LIST;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/department_kind")
public class DepartmentKindController {

    @Autowired
    DepartmentKindService departmentKindService;
    @Autowired
    ConstantVariableService constantVariableService;

    /*
    @GetMapping("/get/{id}")
    public JSONObject getDepartmentInformation(@PathVariable("id") Integer id, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }


        return CommonUtil.successJson(department);
    }*/

    @PostMapping("/add")
    public JSONObject addDepartmentKind(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        DepartmentKind departmentKind = jsonObject.toJavaObject(jsonObject,DepartmentKind.class);

        //检测部门是否存在
        if (departmentKindService.getDepartmentKindByName(departmentKind.getKindName()) != null)
            return CommonUtil.errorJson(ErrorEnum.E_613);

        if (!DEPARTMENT_KIND_LIST.contains(departmentKind.getClassificationId()))
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("部门分类"));

        departmentKindService.save(departmentKind);

        return CommonUtil.successJson(departmentKind);
    }

    @PostMapping("/modify")
    public JSONObject modifyDepartmentKinds(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        DepartmentKind departmentKind = jsonObject.toJavaObject(jsonObject,DepartmentKind.class);

        //检测部门是否存在
        if (departmentKindService.getDepartmentKindByName(departmentKind.getKindName()) != null)
            return CommonUtil.errorJson(ErrorEnum.E_613);

        if (!DEPARTMENT_KIND_LIST.contains(departmentKind.getClassificationId()))
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("部门分类"));

        departmentKindService.update(departmentKind);

        return CommonUtil.successJson(departmentKind);
    }

    @PostMapping("/delete/{id}")
    public JSONObject deleteDepartmentKinds(@PathVariable("id") Integer id, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        DepartmentKind departmentKind = departmentKindService.findById(id);

        //检测部门类型是否存在
        if (departmentKind == null)
            return CommonUtil.errorJson(ErrorEnum.E_612);

        departmentKindService.deleteById(id);

        return CommonUtil.successJson(departmentKind);
    }

    @PostMapping("/getAllDepartmentKind")
    public  JSONObject getNamebyId(){
        List<ConstantVariable> constantVariables = constantVariableService.findAll();
        List<DepartmentKind> departmentKinds = departmentKindService.findAll();

        Object[] results = new Object[2];
        results[0] =constantVariables;
        results[1] = departmentKinds;

        return CommonUtil.successJson(results);
    }



}
