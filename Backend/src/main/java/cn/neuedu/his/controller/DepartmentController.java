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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

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
}
