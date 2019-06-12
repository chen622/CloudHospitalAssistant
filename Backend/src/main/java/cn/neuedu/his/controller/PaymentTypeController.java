package cn.neuedu.his.controller;

import cn.neuedu.his.model.Payment;
import cn.neuedu.his.model.PaymentType;
import cn.neuedu.his.service.PaymentTypeService;
import cn.neuedu.his.service.impl.RedisServiceImpl;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/payment_type")
public class PaymentTypeController {

    @Autowired
    PaymentTypeService paymentTypeService;
    @Autowired
    RedisServiceImpl redisService;

    @PostMapping("/insertPaymentType")
    public JSONObject insertPaymentType(@RequestBody JSONObject jsonObject, Authentication authentication) throws Exception {

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            try{
                PermissionCheck.isFinancialOfficer(authentication);
            }catch (Exception ew){
                return CommonUtil.errorJson(ErrorEnum.E_602);
            }
        }

        PaymentType paymentType = JSONObject.toJavaObject(jsonObject,PaymentType.class);

        Map<String ,Integer> payment=redisService.getMapAll("paymentType");
        //判断类型是否正确
        if(!payment.values().contains(paymentType.getType()))
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("结算类型"));

        try{
            paymentTypeService.save(paymentType);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_605);
        }
        return CommonUtil.successJson(paymentType);
    }

    @PostMapping("/deletePaymentType/{id}")
    public JSONObject deletePaymentType(@PathVariable("id") Integer id, Authentication authentication){

        //检查权限

        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            try{
                PermissionCheck.isFinancialOfficer(authentication);
            }catch (Exception ew){
                return CommonUtil.errorJson(ErrorEnum.E_602);
            }
        }

        PaymentType paymentType = paymentTypeService.findById(id);

        //检查结算类型是否存在
        if (paymentType == null)
            return CommonUtil.errorJson(ErrorEnum.E_606);

        paymentType.setDelete(true);

        paymentTypeService.update(paymentType);

        return CommonUtil.successJson(paymentType);
    }


    @PostMapping("/recoverPaymentType/{id}")
    public JSONObject recoverPaymentType(@PathVariable("id") Integer id, Authentication authentication){

        //检查权限

        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            try{
                PermissionCheck.isFinancialOfficer(authentication);
            }catch (Exception ew){
                return CommonUtil.errorJson(ErrorEnum.E_602);
            }
        }

        PaymentType paymentType = paymentTypeService.findById(id);

        //检查结算类型是否存在
        if (paymentType == null)
            return CommonUtil.errorJson(ErrorEnum.E_606);

        paymentType.setDelete(false);

        paymentTypeService.update(paymentType);

        return CommonUtil.successJson(paymentType);
    }

    @PostMapping("/modifyPaymentType")
    public JSONObject modifyPaymentType(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            try{
                PermissionCheck.isFinancialOfficer(authentication);
            }catch (Exception ew){
                return CommonUtil.errorJson(ErrorEnum.E_602);
            }
        }

        PaymentType paymentType = JSONObject.toJavaObject(jsonObject,PaymentType.class);

        PaymentType lastPaymentType = paymentTypeService.getPaymentTypeByName(paymentType.getName());

        //判断数据是否存在
        if (lastPaymentType != null)
            return CommonUtil.errorJson(ErrorEnum.E_606);

        try{
            paymentType.setId(lastPaymentType.getId());
        }catch (Exception e){
            throw new RuntimeException("信息不能为空");
        }

        return CommonUtil.successJson(paymentType);
    }


    @PostMapping("/updatePaymentType")
    public JSONObject updatePaymentType(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            try{
                PermissionCheck.isFinancialOfficer(authentication);
            }catch (Exception ew){
                return CommonUtil.errorJson(ErrorEnum.E_602);
            }
        }

        PaymentType paymentType = JSONObject.toJavaObject(jsonObject,PaymentType.class);

        PaymentType lastPaymentType = paymentTypeService.getPaymentTypeByName(paymentType.getName());

        //判断数据是否存在
        if (lastPaymentType != null)
            return CommonUtil.errorJson(ErrorEnum.E_606);

        try{
            paymentTypeService.update(paymentType);
        }catch (Exception e){
            throw new RuntimeException("信息不能为空");
        }

        return CommonUtil.successJson();
    }



    @GetMapping("/selectPaymentType/{paymentTypeName}")
    public JSONObject selectPaymentType(@PathVariable("paymentTypeName") String  paymentTypeName, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            try{
                PermissionCheck.isFinancialOfficer(authentication);
            }catch (Exception ew){
                return CommonUtil.errorJson(ErrorEnum.E_602);
            }
        }
        PaymentType paymentType = paymentTypeService.getPaymentTypeByName(paymentTypeName);

        //判断数据是否存在
        if (paymentType != null)
            return CommonUtil.errorJson(ErrorEnum.E_606);

        return CommonUtil.successJson(paymentType);
    }

    @GetMapping("/getAll")
    public JSONObject getAll(){
        try {
            List<PaymentType> list=paymentTypeService.findAll();
            return CommonUtil.successJson(list);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }
    }
}
