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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

        Department department = departmentService.findById(id);

        //检测部门是否存在
        if (department == null)
            return CommonUtil.errorJson(ErrorEnum.E_610);

        departmentService.deleteById(id);

        return CommonUtil.successJson(department);
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

        //检测部门是否存在
        if (departmentService.getDepartmentByName(department.getName()) != null)
            return CommonUtil.errorJson(ErrorEnum.E_611);

        //检测部门类型是否存在
        if (departmentService.findById(department.getKindId()) == null)
            return CommonUtil.errorJson(ErrorEnum.E_612);

        departmentService.save(department);

        return CommonUtil.successJson(department);
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

        //检测部门是否存在
        if (departmentService.getDepartmentByName(department.getName()) != null)
            return CommonUtil.errorJson(ErrorEnum.E_610);

        //检测部门类型是否存在
        if (departmentService.findById(department.getKindId()) == null)
            return CommonUtil.errorJson(ErrorEnum.E_612);

        departmentService.update(department);

        return CommonUtil.successJson(department);
    }

    @GetMapping("/getAllDepartmentKind")
    public  JSONObject getAllDepartmentKind(){
        try {
            //获得科室大类
            List<ConstantVariable> constantVariables = constantVariableService.getDepartmentType(1);
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


}
