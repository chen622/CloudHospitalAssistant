package cn.neuedu.his.controller;

import cn.neuedu.his.model.Drug;
import cn.neuedu.his.service.DrugService;
import cn.neuedu.his.service.impl.RedisServiceImpl;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/drug")
public class DrugController {

    @Autowired
    DrugService drugService;
    @Autowired
    RedisServiceImpl redisService;

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
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
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
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
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

        try{
            drugService.deleteDrug(id);
        }catch (RuntimeException e){
            if (e.getMessage().equals("626"))
                return CommonUtil.errorJson(ErrorEnum.E_626);
        }
        return CommonUtil.successJson();
    }

    @PostMapping("/modify")
    public JSONObject modifyDrug(@RequestBody JSONObject jsonObject , Authentication authentication){

        //检测是药物管理员权限
        try{
            PermissionCheck.getIdByDrugAdmin(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        try{
            Drug drug = JSONObject.toJavaObject(jsonObject,Drug.class);
            drugService.modifyDrug(drug);
        }catch (RuntimeException e){
            if (e.getMessage().equals("626"))
                return CommonUtil.errorJson(ErrorEnum.E_626);
            else if (e.getMessage().equals("627"))
                return CommonUtil.errorJson(ErrorEnum.E_627);
            else if (e.getMessage().equals("628"))
                return CommonUtil.errorJson(ErrorEnum.E_626);
            else if (e.getMessage().equals("631"))
                return CommonUtil.errorJson(ErrorEnum.E_631);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
        return CommonUtil.successJson();
    }

    @PostMapping("/insert")
    public JSONObject insertDrug(@RequestBody JSONObject jsonObject , Authentication authentication){

        //检测是药物管理员权限
        try{
            PermissionCheck.getIdByDrugAdmin(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        Integer id = null;
        try{
            Drug drug = JSONObject.toJavaObject(jsonObject,Drug.class);
            drug.setId(null);
            drugService.insertDrug(drug);
            id=drug.getId();
        }catch (RuntimeException e){
            if (e.getMessage().equals("631"))
                return CommonUtil.errorJson(ErrorEnum.E_631);
            else if (e.getMessage().equals("627"))
                return CommonUtil.errorJson(ErrorEnum.E_627);
            else if (e.getMessage().equals("628"))
                return CommonUtil.errorJson(ErrorEnum.E_628);

        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
        return CommonUtil.successJson(id);

    }


    /***
     * 未结束
     * @param name
     * @param authentication
     * @return
     */
    @GetMapping("/select/{name}")
    public JSONObject selectDrug(@PathVariable("name") String name , Authentication authentication){

        System.out.println(name);

        //检测是药物管理员权限
        try{
            PermissionCheck.getIdByDrugAdmin(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        List<Drug> drugs = drugService.getDrugByName(name);

        //判断药物是否存在
        if (drugs == null)
            return CommonUtil.errorJson(ErrorEnum.E_626);

        return CommonUtil.successJson(drugs);
    }

    /***
     * @return
     */
    @GetMapping("/getAllDrugType")
    public JSONObject getAllDrugType(){
        try {
            Map<String, Integer> map = redisService.getMapAll("drugType");
            JSONObject object = new JSONObject();
            object.put("name",map.keySet());
            object.put("id",map.values());
            return CommonUtil.successJson(object);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
    }


    @GetMapping("/getAllDrug")
    public JSONObject getAllDrug(){
        try {
            return CommonUtil.successJson(drugService.getAllDrug());
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
    }

  }