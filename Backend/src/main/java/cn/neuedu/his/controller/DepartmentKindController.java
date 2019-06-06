package cn.neuedu.his.controller;

import cn.neuedu.his.model.ConstantVariable;
import cn.neuedu.his.model.DepartmentKind;
import cn.neuedu.his.service.ConstantVariableService;
import cn.neuedu.his.service.DepartmentKindService;
import cn.neuedu.his.service.impl.RedisServiceImpl;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    @Autowired
    RedisServiceImpl redisService;

    @PostMapping("/add")
    public JSONObject addDepartmentKind(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        DepartmentKind departmentKind = JSONObject.toJavaObject(jsonObject,DepartmentKind.class);

        //检测部门是否存在
        if (departmentKindService.getDepartmentKindByName(departmentKind.getKindName()) != null)
            return CommonUtil.errorJson(ErrorEnum.E_613);

        try {
            Map<String ,Integer> map=redisService.getMapAll("departmentType");
            if (!map.containsValue(departmentKind.getClassificationId()))
                return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("部门分类"));

            departmentKindService.save(departmentKind);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
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

        DepartmentKind departmentKind = JSONObject.toJavaObject(jsonObject,DepartmentKind.class);

        //检测部门是否存在
        if (departmentKindService.getDepartmentKindByName(departmentKind.getKindName()) != null)
            return CommonUtil.errorJson(ErrorEnum.E_613);

        try {
            Map<String ,Integer> map=redisService.getMapAll("departmentType");
            if (!map.containsValue(departmentKind.getClassificationId()))
                return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("部门分类"));

        } catch (Exception e) {
           return CommonUtil.errorJson(ErrorEnum.E_802);
        }

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

    /**
     * 获得类型及其分类信息
     * @param authentication
     * @return
     */
    @GetMapping("/getAll")
    public JSONObject getDepartmentKindList(Authentication authentication){

        try {
            //获得部门大类
            List<ConstantVariable> constantVariables = constantVariableService.getConstantByType(1);
            JSONObject returnJSON = new JSONObject();
            JSONObject departmentKinds = new JSONObject();

            if (constantVariables != null) {
                returnJSON.put("type", constantVariables);

                constantVariables.forEach(kind -> {
                    List<DepartmentKind> departmentKindList = departmentKindService.getDepartmentKindByClassificationId(kind.getId());
                    departmentKinds.put(kind.getId().toString(),departmentKindList);
                });

                returnJSON.put("departmentKinds", departmentKinds);
            } else {
                returnJSON.put("type", departmentKinds);
                returnJSON.put("departmentKinds", departmentKinds);
            }
            return CommonUtil.successJson(returnJSON);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("数据库连接"));
        }
    }

}
