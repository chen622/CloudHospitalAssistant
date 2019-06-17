package cn.neuedu.his.controller;

import cn.neuedu.his.model.NonDrug;
import cn.neuedu.his.model.PaymentType;
import cn.neuedu.his.service.DepartmentService;
import cn.neuedu.his.service.NonDrugService;
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
@RequestMapping("/non_drug")
public class NonDrugController {

    @Autowired
    NonDrugService nonDrugService;

    @Autowired
    DepartmentService departmentService;

    @PostMapping("/insert")
    public JSONObject insertNonDrug(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHospitalAdmin(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        NonDrug nonDrug = JSONObject.toJavaObject(jsonObject,NonDrug.class);

        try{
            nonDrugService.insertNonDrug(nonDrug);
            return CommonUtil.successJson();
        }catch (RuntimeException e){
            if (e.getMessage().equals("608"))
                return CommonUtil.errorJson(ErrorEnum.E_608);
            else if (e.getMessage().equals("609"))
                return CommonUtil.errorJson(ErrorEnum.E_609);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }

    }

    /*
    @PostMapping("/deleteByName/{name}")
    public JSONObject deleteNonDrugByName(@PathVariable("name") String name, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHospitalAdmin(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        NonDrug nonDrug = jsonObject.toJavaObject(NonDrug.class);

        //检查费药品类型是否存在
        if (NONDRUG_TYPE_LIST.contains(nonDrug.getFeeTypeId()))
            return CommonUtil.errorJson(ErrorEnum.E_608);

        //检查执行部门是否存在
        Integer excutiveDepartmentId = nonDrug.getExcutiveDepartment();
        if (excutiveDepartmentId != null) {
            if (departmentService.findById(excutiveDepartmentId) == null){
                return CommonUtil.errorJson(ErrorEnum.E_609);
            }
        }

        nonDrugService.save(nonDrug);

        return jsonObject;
    }
     */

    @GetMapping("/selectByName/{name}")
    public JSONObject selectNonDrugByName(@PathVariable("name") String name, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHospitalAdmin(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }


        try {
            NonDrug nonDrug = nonDrugService.selectNonDrugUsingName(name);
            return CommonUtil.successJson(nonDrug);

        }catch (RuntimeException e){
            if (e.getMessage().equals("608"))
                return CommonUtil.errorJson(ErrorEnum.E_608);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }

    @GetMapping("/selectByCode/{code}")
    public JSONObject selectNonDrugByCode(@PathVariable("code") String code, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHospitalAdmin(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        try {
            NonDrug nonDrug = nonDrugService.selectNonDrugUsingCode(code);
            return CommonUtil.successJson(nonDrug);
        }catch (RuntimeException e){
            if (e.getMessage().equals("608"))
                return CommonUtil.errorJson(ErrorEnum.E_608);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }

    @PostMapping("/delete/{id}")
    public JSONObject deleteNonDrug(@PathVariable("id") Integer id, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHospitalAdmin(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        try {
            nonDrugService.deleteNonDrug(id);
            return CommonUtil.successJson();
        }catch (RuntimeException e){
            if (e.getMessage().equals("608"))
                return CommonUtil.errorJson(ErrorEnum.E_608);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }

    @PostMapping("/modify")
    public JSONObject modifyNonDrug(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHospitalAdmin(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        try {
            NonDrug nonDrug = JSONObject.toJavaObject(jsonObject, NonDrug.class);
            nonDrugService.modifyNonDrug(nonDrug);
            return CommonUtil.successJson();
        }catch (RuntimeException e){
            if (e.getMessage().equals("609"))
                return CommonUtil.errorJson(ErrorEnum.E_609);
            else
                return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }

    /**
     * 根据非药品名称来模糊查询
     * 根据非药品编码来模糊查询
     * 获取所有type和非药品目录
     * @param name
     * @param code
     * @param authentication
     * @return
     */
    @GetMapping({"/getTypeAndNonDrug/name/{name}","/getTypeAndNonDrug/code/{code}",
            "/getTypeAndNonDrug/nameAndCode/{name}/{code}","/getTypeAndNonDrug/"})
    JSONObject getTypeAndNonDrug(@PathVariable(value = "name",required = false) String name, @PathVariable(value = "code",required = false) String code, Authentication authentication){

        Boolean auth;
        Integer departmentId = null;
        //判断权限
        try {
            PermissionCheck.isHosptialAdimReturnUserId(authentication);
            auth = true;
        } catch (Exception e) {
            auth = false;
        }


        List<PaymentType> paymentTypes = nonDrugService.getTypeAndNonDrug(name,code,auth);

        return CommonUtil.successJson(paymentTypes);
    }
}
