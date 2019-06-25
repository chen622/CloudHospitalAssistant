package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.ConstantVariableRemote;
import cn.neuedu.consumer.remoteInterface.GeneralRemote;
import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/constant_variable")
public class ConstantVariableController {
//    @Autowired
//    ConstantVariableRemote constantVariableRemote;

    @Autowired
    GeneralRemote generalRemote;
    @GetMapping("/get")
    public JSONObject get() {
        return generalRemote.get();
    }

//    @GetMapping("/get")
//    public JSONObject get(Authentication authentication) {
//        return constantVariableRemote.get(authentication);
//    }
//
//    @PostMapping("/getName/{id}")
//    public JSONObject getNameById(@PathVariable("id") Integer id) {
//        return constantVariableRemote.getNameById(id);
//    }
//
//    @PostMapping("/insert/{type}")
//    public JSONObject insertConstant(@RequestBody JSONObject jsonObject, @PathVariable("type") String type, Authentication authentication) {
//        return constantVariableRemote.insertConstant(jsonObject, type, authentication);
//    }

//    @PostMapping("/delete/{type}/{id}")
//    public JSONObject deleteConstant(@PathVariable("id") Integer id, @PathVariable("type") String type, Authentication authentication) {
//        try {
//            PermissionCheck.isHospitalAdmin(authentication);
//        } catch (Exception e) {
//            return CommonUtil.errorJson(ErrorEnum.E_602);
//        }
//
//        try {
//            constantVariableService.deleteConstant(id, type);
//        } catch (RuntimeException e) {
//            if (e.getMessage().equals("629"))
//                return CommonUtil.errorJson(ErrorEnum.E_629);
//            else if (e.getMessage().equals("633"))
//                return CommonUtil.errorJson(ErrorEnum.E_633);
//        } catch (Exception e) {
//            return CommonUtil.errorJson(ErrorEnum.E_802);
//        }
//        return CommonUtil.successJson();
//    }
//
//    @PostMapping("/modify/{type}")
//    public JSONObject modifyConstant(@RequestBody JSONObject jsonObject, @PathVariable("type") String type, Authentication authentication) {
//        try {
//            PermissionCheck.isHospitalAdmin(authentication);
//        } catch (Exception e) {
//            return CommonUtil.errorJson(ErrorEnum.E_602);
//        }
//
//        try {
//            ConstantVariable constantVariable = JSONObject.toJavaObject(jsonObject, ConstantVariable.class);
//            constantVariableService.modifyConstant(constantVariable, type);
//        } catch (RuntimeException e) {
//            if (e.getMessage().equals("629"))
//                return CommonUtil.errorJson(ErrorEnum.E_629);
//            else if (e.getMessage().equals("633"))
//                return CommonUtil.errorJson(ErrorEnum.E_633);
//        } catch (Exception e) {
//            return CommonUtil.errorJson(ErrorEnum.E_802);
//        }
//        return CommonUtil.successJson();
//    }
//
//    @GetMapping("/getSettlementType")
//    public JSONObject getSettlementType() {
//        try {
//            Map<String, Integer> map = redisService.getMapAll("settlementType");
//            JSONArray array = new JSONArray();
//            map.forEach((k, v) -> {
//                        JSONObject jsonObject = new JSONObject();
//                        jsonObject.put("id", v);
//                        jsonObject.put("name", k);
//                        array.add(jsonObject);
//                    }
//            );
//            return CommonUtil.successJson(array);
//        } catch (Exception e) {
//            return CommonUtil.errorJson(ErrorEnum.E_500);
//        }
//    }
//
//    @GetMapping("/getForm")
//    public JSONObject getDrugForm() {
//        try {
//            Map<String, Integer> map = redisService.getMapAll("formulation");
//            ArrayList<Map> list = new ArrayList<>();
//            for (String string : map.keySet()) {
//                Map map1 = new HashMap();
//                map1.put("name", string);
//                map1.put("id", map.get(string));
//                list.add(map1);
//            }
//            return CommonUtil.successJson(list);
//        } catch (Exception e) {
//            return CommonUtil.errorJson(ErrorEnum.E_802);
//        }
//    }
//
//    @GetMapping("/getType/{type}")
//    public JSONObject getConstantByType(@PathVariable("type") Integer typeId, Authentication authentication) {
//
//
//        try {
//            List<ConstantVariable> list = new ArrayList<>();
//            String type = null;
//            Map<String, Integer> map = redisService.getMapAll("typeKind");
//            for (Map.Entry<String, Integer> entry : map.entrySet()) {
//                if (entry.getValue().equals(typeId)) {
//                    type = entry.getKey();
//                    break;
//                }
//            }
//
//            map = redisService.getMapAll(type);
//            ConstantVariable constantVariable;
//
//            for (Map.Entry<String, Integer> entry : map.entrySet()) {
//                constantVariable = new ConstantVariable();
//                constantVariable.setName(entry.getKey());
//                constantVariable.setId(entry.getValue());
//                list.add(constantVariable);
//            }
//            return CommonUtil.successJson(list);
//
//        } catch (Exception e) {
//            return CommonUtil.errorJson(ErrorEnum.E_802);
//        }
//    }
//
//
//    @GetMapping("/getAll")
//    public JSONObject getAll() {
//
//        List<ConstantVariable> constantVariables = constantVariableService.findAll();
//        return CommonUtil.successJson(constantVariables);
//    }

}
