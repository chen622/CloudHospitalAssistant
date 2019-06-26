package cn.neuedu.his.controller;

import cn.neuedu.his.model.ConstantVariable;
import cn.neuedu.his.service.ConstantVariableService;
import cn.neuedu.his.service.DepartmentService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/general")
public class GeneralController {
    @Autowired
    ConstantVariableService constantVariableService;
    @Autowired
    DepartmentService departmentService;


    @GetMapping("/getAllDepartmentKind")
    public JSONObject getAllDepartmentKind() {
        try {
            //获得科室大类
            List<ConstantVariable> constantVariables = constantVariableService.getDepartmentType();
            JSONObject returnJSON = new JSONObject();
            JSONArray departments = new JSONArray();
            if (constantVariables != null) {
                returnJSON.put("type", constantVariables);
                //获得分别与大类对应的科室信息
                constantVariables.forEach(kind -> {
                    departments.add(departmentService.getAllDepartmentInformationByClassificationId(kind.getId()));
                });
                //{kind:[],depas:[[],[]] }

                returnJSON.put("departments", departments);
            } else {
                returnJSON.put("type", departments);
                returnJSON.put("departments", departments);
            }
            return CommonUtil.successJson(returnJSON);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("数据库连接"));
        }
    }
}
