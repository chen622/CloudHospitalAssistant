package cn.neuedu.his.controller;

import cn.neuedu.his.model.ConstantVariable;
import cn.neuedu.his.model.Department;
import cn.neuedu.his.service.ConstantVariableService;
import cn.neuedu.his.service.DepartmentKindService;
import cn.neuedu.his.service.DepartmentService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;
    @Autowired
    DepartmentKindService departmentKindService;
    @Autowired
    ConstantVariableService constantVariableService;

    /**
     * 获得部门的详细信息
     * @return
     */
    @GetMapping("/get")
    public JSONObject getDepartmentInformation(){
        try {
            List<Department> departments = departmentService.getDepartmentInformation();
            return CommonUtil.successJson(departments);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("数据库连接"));
        }

    }

    @PostMapping("/delete/{id}")
    public JSONObject deleteDepartmentInformation(@PathVariable("id") Integer id, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }


        try{
            departmentService.deleteDepartmentInformation(id);
            return CommonUtil.successJson();
        }catch (RuntimeException e){
            if (e.getMessage().equals("610"))
                return CommonUtil.errorJson(ErrorEnum.E_610);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }

    @PostMapping("/add")
    public JSONObject addDepartment(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        Department department = jsonObject.toJavaObject(jsonObject,Department.class);

        try{
            departmentService.addDepartment(department);
            return CommonUtil.successJson();
        }catch (RuntimeException e){
            if (e.getMessage().equals("611"))
                return CommonUtil.errorJson(ErrorEnum.E_611);
            else if (e.getMessage().equals("612"))
                return CommonUtil.errorJson(ErrorEnum.E_612);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }

    @PostMapping("/modify")
    public JSONObject modifyDepartment(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        Department department = jsonObject.toJavaObject(jsonObject,Department.class);

        try{
            departmentService.addDepartment(department);
            return CommonUtil.successJson();
        }catch (RuntimeException e){
            if (e.getMessage().equals("610"))
                return CommonUtil.errorJson(ErrorEnum.E_610);
            else if (e.getMessage().equals("612"))
                return CommonUtil.errorJson(ErrorEnum.E_612);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }

    @GetMapping("/getAllDepartmentKind")
    public  JSONObject getAllDepartmentMatchKind(){
        try {
            //获得科室大类
            List<ConstantVariable> constantVariables = constantVariableService.getDepartmentType();
            JSONObject returnJSON = new JSONObject();
            returnJSON.put("type", constantVariables);

            JSONArray departments = new JSONArray();
            //获得分别与大类对应的科室信息
            constantVariables.forEach(kind -> {
                departments.add(departmentService.getAllDepartmentInformationByClassificationId(kind.getId()));
            });
            //{kind:[],depas:[[],[]] }
            returnJSON.put("departments", departments);
            return CommonUtil.successJson(returnJSON);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("数据库连接"));
        }
    }

//    @GetMapping("/billingDeptDayKnot/{start}/{end}")
//    public JSONObject billingDeptDayKnot(@PathVariable("start") Date start,@PathVariable("end") Date end ,Authentication authentication){
//        Integer operatorId;
//        try {
//            operatorId = PermissionCheck.isFinancialOfficer(authentication);
//        } catch (AuthenticationServiceException a) {
//            return CommonUtil.errorJson(ErrorEnum.E_502.addErrorParamName(a.getMessage()));
//        }
//    }

}
