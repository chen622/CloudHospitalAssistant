package cn.neuedu.his.controller;

import cn.neuedu.his.model.DiseaseFirst;
import cn.neuedu.his.service.DiseaseFirstService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/disease_first")
public class DiseaseFirstController {

    @Autowired
    DiseaseFirstService diseaseFirstService;

    @PostMapping("/delete/{id}")
    public JSONObject deleteDiseaseFirst(@PathVariable Integer id, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        diseaseFirstService.deleteById(id);

        return CommonUtil.successJson(id);
    }

    @PostMapping("/insert")
    public JSONObject insertDiseaseFirst(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        DiseaseFirst diseaseFirst = jsonObject.toJavaObject(jsonObject,DiseaseFirst.class);

        //判断类别名称是否重复
        if (diseaseFirstService.getDiseaseByname(diseaseFirst.getName()) != null)
            return CommonUtil.errorJson(ErrorEnum.E_620);

        diseaseFirstService.save(diseaseFirst);

        return CommonUtil.successJson(diseaseFirst);
    }

    @PostMapping("/modify")
    public JSONObject modifyDiseaseFirst(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        DiseaseFirst diseaseFirst = jsonObject.toJavaObject(jsonObject,DiseaseFirst.class);

        if (diseaseFirstService.findById(diseaseFirst.getId()) == null)
            return CommonUtil.errorJson(ErrorEnum.E_620);

        //判断疾病是否存在
        if (diseaseFirstService.getDiseaseByname(diseaseFirst.getName()) == null)
            return CommonUtil.errorJson(ErrorEnum.E_620);

        diseaseFirstService.update(diseaseFirst);

        return CommonUtil.successJson(diseaseFirst);
    }

    @PostMapping("/select/{name}")
    public JSONObject selectDiseaseFirst(@PathVariable("name") String name, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        DiseaseFirst diseaseFirst = diseaseFirstService.getDiseaseByname(name);

        //判断疾病是否存在
        if (diseaseFirst == null)
            return CommonUtil.errorJson(ErrorEnum.E_620);

        return CommonUtil.successJson(diseaseFirst);
    }

}
