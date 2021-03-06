package cn.neuedu.his.controller;

import cn.neuedu.his.model.DiseaseFirst;
import cn.neuedu.his.service.DiseaseFirstService;
import cn.neuedu.his.service.impl.RedisServiceImpl;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
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
@RequestMapping("/disease_first")
public class DiseaseFirstController {

    @Autowired
    DiseaseFirstService diseaseFirstService;
    @Autowired
    RedisServiceImpl redisService;

    @PostMapping("/delete/{id}")
    public JSONObject deleteDiseaseFirst(@PathVariable Integer id, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHospitalAdmin(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        try{
            diseaseFirstService.deleteDiseaseFirst(id);
            return CommonUtil.successJson();
        }catch (RuntimeException e){
            if (e.getMessage().equals("624"))
                return CommonUtil.errorJson(ErrorEnum.E_624);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        }

    }

    @PostMapping("/insert")
    public JSONObject insertDiseaseFirst(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHospitalAdmin(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        DiseaseFirst diseaseFirst = JSONObject.toJavaObject(jsonObject,DiseaseFirst.class);

        try {
            diseaseFirstService.insertDiseaseFirst(diseaseFirst);
            return CommonUtil.successJson();
        }catch (RuntimeException e){
            if (e.getMessage().equals("620"))
                return CommonUtil.errorJson(ErrorEnum.E_620);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }

    @PostMapping("/modify")
    public JSONObject modifyDiseaseFirst(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHospitalAdmin(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        DiseaseFirst diseaseFirst = JSONObject.toJavaObject(jsonObject,DiseaseFirst.class);

        try{
            diseaseFirstService.modifyDiseaseFirst(diseaseFirst);
            return CommonUtil.successJson();
        } catch (RuntimeException e){
            if (e.getMessage().equals("620"))
                return CommonUtil.errorJson(ErrorEnum.E_620);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }

    @PostMapping("/select/{name}")
    public JSONObject selectDiseaseFirst(@PathVariable("name") String name, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHospitalAdmin(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        try{
            DiseaseFirst diseaseFirst = diseaseFirstService.selectDiseaseFirst(name);
            return CommonUtil.successJson(diseaseFirst);
        }catch (RuntimeException e){
            if (e.getMessage().equals("620"))
                return CommonUtil.errorJson(ErrorEnum.E_620);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }

    /**
     * 1. 按照类别姓名来模糊查询
     * 2. 医院管理员获得所有数据
     * 3. 普通医生只能获得未删除的数据
     * 4. name为空获得所有数据
     * @param name
     * @param authentication
     * @return
     */
    @GetMapping({"/getDiseae/{name}","/getType"})
    JSONObject getAllDiseaseType(@PathVariable(value = "name",required = false) String name, Authentication authentication){

        Boolean auth;
        //判断权限
        try {
            PermissionCheck.isHospitalAdmin(authentication);
            auth = true;
        } catch (Exception e) {
            auth = false;
        }

        List<DiseaseFirst> diseaseFirsts = diseaseFirstService.getAllDiseaseType(auth,name);

        return CommonUtil.successJson(diseaseFirsts);
    }

}
