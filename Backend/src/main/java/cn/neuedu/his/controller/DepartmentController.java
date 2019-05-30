package cn.neuedu.his.controller;

import cn.neuedu.his.model.Department;
import cn.neuedu.his.service.DepartmentService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    /**
     * 获得一个部门的详细信息
     * @param id
     * @param authentication
     * @return
     */
    @GetMapping("/get/{id}")
    public JSONObject getDepartmentInformation(@PathVariable("id") Integer id, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        Department department = departmentService.getAllDepartmentInformation(id);

        if (department == null)
            return CommonUtil.errorJson(ErrorEnum.E_610);

        return CommonUtil.successJson(department);
    }

    @PostMapping("/delete/{id}")
    public JSONObject deleteDepartmentInformation(@PathVariable("id") Integer id, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        Department department = departmentService.getAllDepartmentInformation(id);

        //检测部门是否存在
        if (department == null)
            return CommonUtil.errorJson(ErrorEnum.E_610);

        departmentService.deleteById(id);

        return CommonUtil.successJson(department);
    }


}
