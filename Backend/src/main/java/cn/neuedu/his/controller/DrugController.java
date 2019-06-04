package cn.neuedu.his.controller;

import cn.neuedu.his.model.Drug;
import cn.neuedu.his.service.DrugService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static cn.neuedu.his.util.constants.Constants.DRUG_TYPE_LIST;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/drug")
public class DrugController {

    @Autowired
    DrugService drugService;

    /**
     * 发药
     * @param paymentId
     * @param drugId
     * @param authentication
     * @return
     */
    @PostMapping("/takeDrug/{paymentId}/{drugId}")
    public JSONObject takeDrug(@PathVariable("paymentId") Integer paymentId, @PathVariable("drugId") Integer drugId, Authentication authentication) {
        Integer drugAdmin;
        try {
            drugAdmin = PermissionCheck.getIdByDrugAdmin(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        try {
            drugService.takeDrug(paymentId, drugId, drugAdmin);
        }catch (IllegalArgumentException e1) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e1.getMessage()));
        }catch (UnsupportedOperationException e2) {
            return CommonUtil.errorJson(ErrorEnum.E_506);
        }

        return CommonUtil.successJson();
    }

    /**
     * 退药
     * @param jsonObject
     * @param authentication
     * @return
     */
    @PostMapping("/retreatDrug")
    public JSONObject retreatDrug(@RequestBody JSONObject jsonObject, Authentication authentication) {
        Integer drugAdmin;
        try {
            drugAdmin = PermissionCheck.getIdByDrugAdmin(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        try {
            drugService.retreatDrug(jsonObject.getInteger("paymentId"), jsonObject.getInteger("drugId"), jsonObject.getInteger("quantity"), drugAdmin);
        }catch (IllegalArgumentException e1) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e1.getMessage()));
        }catch (UnsupportedOperationException e2) {
            if (e2.getMessage().equals("paymentState"))
                return CommonUtil.errorJson(ErrorEnum.E_506);
            else if (e2.getMessage().equals("paymentType"))
                return CommonUtil.errorJson(ErrorEnum.E_504);
        } catch (IndexOutOfBoundsException e3) {
            return CommonUtil.errorJson(ErrorEnum.E_507);
        }

        return CommonUtil.successJson();
    }


    @PostMapping("/delete/{id}")
    public JSONObject deleteDrug(@PathVariable("id") Integer id , Authentication authentication){

        //检测是药物管理员权限
        try{
            PermissionCheck.getIdByDrugAdmin(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        Drug drug = drugService.findById(id);

        //判断药物是否存在
        if (drug == null)
            return CommonUtil.errorJson(ErrorEnum.E_626);

        drugService.deleteById(id);

        return CommonUtil.successJson(id);
    }

    @PostMapping("/modify")
    public JSONObject modifyDrug(@RequestBody JSONObject jsonObject , Authentication authentication){

        //检测是药物管理员权限
        try{
            PermissionCheck.getIdByDrugAdmin(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        Drug drug = jsonObject.toJavaObject(jsonObject,Drug.class);

        //判断药物是否存在
        if (drugService.findById(drug.getId()) == null)
            return CommonUtil.errorJson(ErrorEnum.E_626);

        //判断药物类别是否正确
        if (DRUG_TYPE_LIST.contains(drug))
            return CommonUtil.errorJson(ErrorEnum.E_627);

        drugService.update(drug);

        return CommonUtil.successJson(drug);
    }

    @PostMapping("/insert")
    public JSONObject insertDrug(@RequestBody JSONObject jsonObject , Authentication authentication){

        //检测是药物管理员权限
        try{
            PermissionCheck.getIdByDrugAdmin(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        Drug drug = jsonObject.toJavaObject(jsonObject,Drug.class);

        //判断药物类别是否正确
        if (DRUG_TYPE_LIST.contains(drug))
            return CommonUtil.errorJson(ErrorEnum.E_627);

        drugService.update(drug);

        return CommonUtil.successJson(drug);
    }
}
