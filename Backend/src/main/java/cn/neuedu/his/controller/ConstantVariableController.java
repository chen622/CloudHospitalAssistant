package cn.neuedu.his.controller;

import cn.neuedu.his.model.ConstantVariable;
import cn.neuedu.his.service.ConstantVariableService;
import cn.neuedu.his.service.impl.RedisServiceImpl;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.ConditionalOnRepositoryType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/constant_variable")
public class ConstantVariableController {

    @Autowired
    ConstantVariableService constantVariableService;
    @Autowired
    RedisServiceImpl redisService;

    @GetMapping("/get")
    public JSONObject get(Authentication authentication) {
        Integer userId = PermissionCheck.getIdByUser(authentication);
        return CommonUtil.successJson(constantVariableService.findAll());
    }

    @PostMapping("/getName")
    public JSONObject getNamebyId(Integer id) {
        return CommonUtil.successJson(constantVariableService.findById(id).getName());
    }

    /**
     * 增加常量小类(type 及以后)
     *
     * @param jsonObject
     * @param authentication
     * @return
     */
    @PostMapping("/insert")
    public JSONObject insertConstant(@RequestBody JSONObject jsonObject, Authentication authentication) {
        try {
            PermissionCheck.isHosptialAdim(authentication);
            ConstantVariable constantVariable = JSONObject.toJavaObject(jsonObject, ConstantVariable.class);
            constantVariableService.insertConstant(constantVariable);
            return CommonUtil.successJson();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("629"))
                return CommonUtil.errorJson(ErrorEnum.E_629);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
    }

    @PostMapping("/delete")
    public JSONObject deleteConstant(@PathVariable("id") Integer id, Authentication authentication) {
        try {
            PermissionCheck.isHosptialAdim(authentication);
            constantVariableService.deleteConstant(id);
            return CommonUtil.successJson();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("629"))
                return CommonUtil.errorJson(ErrorEnum.E_629);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
    }

    @PostMapping("/modify")
    public JSONObject modifyConstant(@RequestBody JSONObject jsonObject, Authentication authentication) {
        try {
            PermissionCheck.isHosptialAdim(authentication);
            ConstantVariable constantVariable = JSONObject.toJavaObject(jsonObject, ConstantVariable.class);
            constantVariableService.modifyConstant(constantVariable);
            return CommonUtil.successJson();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("629"))
                return CommonUtil.errorJson(ErrorEnum.E_629);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
    }

    @GetMapping("/getSettlementType")
    public JSONObject getSettlementType() {
        try {
            Map<String, Integer> map = redisService.getMapAll("settlementType");
            JSONArray array = new JSONArray();
            map.forEach((k, v) -> {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", v);
                        jsonObject.put("name", k);
                        array.add(jsonObject);
                    }
            );
            return CommonUtil.successJson(array);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }

    @GetMapping("/getForm")
    public JSONObject getDrugForm() {
        try {
            Map<String, Integer> map = redisService.getMapAll("formulation");
            ArrayList<Map> list = new ArrayList<>();
            for (String string:map.keySet()){
                Map map1 = new HashMap();
                map1.put("name", string);
                map1.put("id", map.get(string));
                list.add(map1);
            }
            return CommonUtil.successJson(list);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
    }

    @GetMapping("/getType/{type}")
    public JSONObject getConstantByType(@PathVariable("type") Integer type){

        List<ConstantVariable> constantVariables = constantVariableService.getConstantByType(type);
        return CommonUtil.successJson(constantVariables);
    }

}
