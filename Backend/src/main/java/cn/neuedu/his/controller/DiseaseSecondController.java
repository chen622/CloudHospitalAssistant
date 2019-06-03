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
    public JSONObject insertDiseaseFirst(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        DiseaseSecond diseaseSecond = jsonObject.toJavaObject(jsonObject,DiseaseSecond.class);

        //判断国际编码是否重复
        if (diseaseSecondService.findByIcdId(diseaseSecond.getIcdId()) != null)
            return CommonUtil.errorJson(ErrorEnum.E_621);

        //判断疾病编码是否重复
        if (diseaseSecondService.findByDiseaseCoding(diseaseSecond.getDiseaseCoding()) != null)
            return CommonUtil.errorJson(ErrorEnum.E_622);

        //判断疾病名称是否重复
        if (diseaseSecondService.findByName(diseaseSecond.getName()) != null)
            return CommonUtil.errorJson(ErrorEnum.E_623);

        //判断疾病类别是否存在
        if (diseaseFirstService.findById(diseaseSecond.getDiseaseFirstId()) != null)
            return CommonUtil.errorJson(ErrorEnum.E_624);

        diseaseSecondService.save(diseaseSecond);

        return CommonUtil.successJson(diseaseSecond);
    }

    @PostMapping("/delete/{id}")
    public JSONObject delateDiseaseFirst(@PathVariable("id") Integer id, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        //判断疾病是否存在
        if (diseaseSecondService.findById(id) == null)
            return CommonUtil.errorJson(ErrorEnum.E_625);

        diseaseSecondService.deleteById(id);

        return CommonUtil.successJson(id);
    }

    @PostMapping("/select/{name}")
    public JSONObject selectDiseaseFirst(@PathVariable("id") String name, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        List<DiseaseSecond> diseaseSeconds = diseaseSecondService.findByName(name);

        if (diseaseSeconds == null)
            return CommonUtil.errorJson(ErrorEnum.E_625);

        return CommonUtil.successJson(diseaseSeconds);
    }

    @PostMapping("/modify")
    public JSONObject modifyDiseaseFirst(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        DiseaseSecond diseaseSecond = jsonObject.toJavaObject(jsonObject,DiseaseSecond.class);

        if (diseaseSecondService.findById(diseaseSecond.getId()) == null)
            return CommonUtil.errorJson(ErrorEnum.E_625);

        //判断国际编码是否存在
        if (diseaseSecondService.findByIcdId(diseaseSecond.getIcdId()) == null)
            return CommonUtil.errorJson(ErrorEnum.E_621);

        //判断疾病编码是否存在
        if (diseaseSecondService.findByDiseaseCoding(diseaseSecond.getDiseaseCoding()) == null)
            return CommonUtil.errorJson(ErrorEnum.E_622);

        //判断疾病类别是否存在
        if (diseaseFirstService.findById(diseaseSecond.getDiseaseFirstId()) == null)
            return CommonUtil.errorJson(ErrorEnum.E_624);

        diseaseSecondService.update(diseaseSecond);

        return CommonUtil.successJson(diseaseSecond);
    }

}
