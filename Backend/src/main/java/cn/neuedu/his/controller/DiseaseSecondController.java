package cn.neuedu.his.controller;

import cn.neuedu.his.model.DiseaseSecond;
import cn.neuedu.his.service.DiseaseFirstService;
import cn.neuedu.his.service.DiseaseSecondService;
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
@RequestMapping("/disease_second")
public class DiseaseSecondController {

    @Autowired
    DiseaseSecondService diseaseSecondService;
    @Autowired
    DiseaseFirstService diseaseFirstService;

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

    @PostMapping("/select/{name}")
    public JSONObject selectDiseaseSecond(@PathVariable("name") String name, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        try{
            List<DiseaseSecond> diseaseSeconds = diseaseSecondService.selectDiseaseSecond(name);
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
            return CommonUtil.successJson();
        }catch (RuntimeException e){
            if (e.getMessage().equals("621"))
                return CommonUtil.errorJson(ErrorEnum.E_621);
            else if (e.getMessage().equals("622"))
                return CommonUtil.errorJson(ErrorEnum.E_622);
            else if (e.getMessage().equals("624"))
                return CommonUtil.errorJson(ErrorEnum.E_624);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }

}
