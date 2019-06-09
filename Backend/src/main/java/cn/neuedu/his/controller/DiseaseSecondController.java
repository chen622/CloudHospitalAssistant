package cn.neuedu.his.controller;

import cn.neuedu.his.model.DiseaseSecond;
import cn.neuedu.his.service.DiseaseFirstService;
import cn.neuedu.his.service.DiseaseSecondService;
import cn.neuedu.his.service.impl.RedisServiceImpl;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/disease_second")
public class DiseaseSecondController {

    @Autowired
    DiseaseSecondService diseaseSecondService;
    @Autowired
    DiseaseFirstService diseaseFirstService;
    @Autowired
    RedisServiceImpl redisService;

    @PostMapping("/insert")
    public JSONObject insertDiseaseSecond(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        DiseaseSecond diseaseSecond = JSONObject.toJavaObject(jsonObject,DiseaseSecond.class);

        try{
            diseaseSecondService.insertDiseaseSecond(diseaseSecond);
            return CommonUtil.successJson();
        }catch (RuntimeException e){
            if (e.getMessage().equals("621"))
                return CommonUtil.errorJson(ErrorEnum.E_621);
            else if (e.getMessage().equals("622"))
                return CommonUtil.errorJson(ErrorEnum.E_622);
            else if (e.getMessage().equals("623"))
                return CommonUtil.errorJson(ErrorEnum.E_623);
            else if (e.getMessage().equals("624"))
                return CommonUtil.errorJson(ErrorEnum.E_624);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }

    @PostMapping("/delete/{id}")
    public JSONObject delateDiseaseSecond(@PathVariable("id") Integer id, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        try{
            diseaseSecondService.delateDiseaseSecond(id);
            return CommonUtil.successJson(id);
        }catch (RuntimeException e){
            if (e.getMessage().equals("625"))
                return CommonUtil.errorJson(ErrorEnum.E_625);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }

    @GetMapping("/selectByName/{name}")
    public JSONObject selectDiseaseSecondByName(@PathVariable("name") String name, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        try{
            List<DiseaseSecond> diseaseSeconds = diseaseSecondService.findByName(name);
            return CommonUtil.successJson(diseaseSeconds);

        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }

    @GetMapping("/selectByIcd/{icdId}")
    public JSONObject selectDiseaseSecondByIcd(@PathVariable("icdId") String icdId, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        try{
            List<DiseaseSecond> diseaseSeconds = diseaseSecondService.findByIcdId(icdId);
            return CommonUtil.successJson(diseaseSeconds);

        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }

    @PostMapping("/modify")
    public JSONObject modifyDiseaseSecond(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        DiseaseSecond diseaseSecond = JSONObject.toJavaObject(jsonObject,DiseaseSecond.class);

        try{
            diseaseSecondService.modifyDiseaseSecond(diseaseSecond);
        }catch (RuntimeException e){
            if (e.getMessage().equals("621"))
                return CommonUtil.errorJson(ErrorEnum.E_621);
            else if (e.getMessage().equals("622"))
                return CommonUtil.errorJson(ErrorEnum.E_622);
            else if (e.getMessage().equals("624"))
                return CommonUtil.errorJson(ErrorEnum.E_624);
        }
        return CommonUtil.successJson();
    }

    /**
     * 1. 医院管理员获得所有数据
     * 2. 普通医生只能获得未删除的数据
     * 3. type为空获得所有数据
     * @param type
     * @param authentication
     * @return
     */
    @GetMapping({"/getDiseaseByType/{type}","/getDiseaseByType"})
    JSONObject getDiseaseByType(@PathVariable(value = "type",required = false) Integer type, Authentication authentication){

        Boolean auth;
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("typeId");
        Map<String, Integer> map = null;
        try {
            map = redisService.getMapAll("userType");
            if (typeId.equals(map.get("医院管理员"))) {
                auth = true;
            } else {
                auth = false;
            }
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }

        List<DiseaseSecond> diseaseSeconds = diseaseSecondService.findByDiseaseFirstid(auth,type);

        return CommonUtil.successJson(diseaseSeconds);
    }
}
