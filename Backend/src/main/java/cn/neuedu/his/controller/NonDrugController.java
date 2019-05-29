package cn.neuedu.his.controller;

import cn.neuedu.his.model.NonDrug;
import cn.neuedu.his.service.DepartmentService;
import cn.neuedu.his.service.NonDrugService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static cn.neuedu.his.util.constants.Constants.NONDRUG_TYPE_LIST;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/non_drug")
public class NonDrugController {

    @Autowired
    NonDrugService nonDrugService;

    @Autowired
    DepartmentService departmentService;

    @PostMapping("/insert")
    public JSONObject insertNonDrug(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        NonDrug nonDrug = jsonObject.toJavaObject(NonDrug.class);

        //检查费药品类型是否存在
        if (NONDRUG_TYPE_LIST.contains(nonDrug.getFeeTypeId()))
            return CommonUtil.errorJson(ErrorEnum.E_608);

        //检查执行部门是否存在
        Integer excutiveDepartmentId = nonDrug.getExcutiveDepartment();
        if (excutiveDepartmentId != null) {
            if (departmentService.findById(excutiveDepartmentId) == null){
                return CommonUtil.errorJson(ErrorEnum.E_609);
            }
        }

        nonDrugService.save(nonDrug);

        return CommonUtil.successJson(nonDrug);
    }

    /*
    @PostMapping("/deleteByName/{name}")
    public JSONObject deleteNonDrugByName(@PathVariable("name") String name, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        NonDrug nonDrug = jsonObject.toJavaObject(NonDrug.class);

        //检查费药品类型是否存在
        if (NONDRUG_TYPE_LIST.contains(nonDrug.getFeeTypeId()))
            return CommonUtil.errorJson(ErrorEnum.E_608);

        //检查执行部门是否存在
        Integer excutiveDepartmentId = nonDrug.getExcutiveDepartment();
        if (excutiveDepartmentId != null) {
            if (departmentService.findById(excutiveDepartmentId) == null){
                return CommonUtil.errorJson(ErrorEnum.E_609);
            }
        }

        nonDrugService.save(nonDrug);

        return jsonObject;
    }
     */

    @GetMapping("/selectByName/{name}")
    public JSONObject selectNonDrugByName(@PathVariable("name") String name, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        NonDrug nonDrug = nonDrugService.selectNonDrugByName(name);

        //检查非药品是否存在
        if (nonDrug == null)
            return CommonUtil.errorJson(ErrorEnum.E_608);

        return CommonUtil.successJson(nonDrug);
    }

    @GetMapping("/selectByCode/{code}")
    public JSONObject selectNonDrugByCode(@PathVariable("name") String code, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        NonDrug nonDrug = nonDrugService.selectNonDrugByCode(code);

        //检查非药品是否存在
        if (nonDrug == null)
            return CommonUtil.errorJson(ErrorEnum.E_608);

        return CommonUtil.successJson(nonDrug);
    }
}
