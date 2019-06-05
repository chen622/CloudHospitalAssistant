package cn.neuedu.his.controller;

import cn.neuedu.his.model.Payment;
import cn.neuedu.his.model.PaymentType;
import cn.neuedu.his.service.PaymentTypeService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static cn.neuedu.his.util.constants.Constants.PAYMENT_TYPE_LIST;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/payment_type")
public class PaymentTypeController {

    @Autowired
    PaymentTypeService paymentTypeService;

    @PostMapping("/insertPaymentType")
    public JSONObject insertPaymentType(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        PaymentType paymentType = JSONObject.toJavaObject(jsonObject,PaymentType.class);

        //判断类型是否正确
        if(PAYMENT_TYPE_LIST.contains(paymentType.getType()))
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("结算类型"));

        try{
            paymentTypeService.save(paymentType);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_605);
        }
        return CommonUtil.successJson(paymentType);
    }

    @PostMapping("/deletePaymentType/{paymentTypeName}")
    public JSONObject deletePaymentType(@PathVariable("paymentTypeName") String paymentTypeName, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        PaymentType paymentType = paymentTypeService.getPaymentTypeByName(paymentTypeName);

        //检查结算类型是否存在
        if (paymentType == null)
            return CommonUtil.errorJson(ErrorEnum.E_606);

        paymentTypeService.deleteById(paymentType.getId());

        return CommonUtil.successJson(paymentType);
    }

    @PostMapping("/modifyPaymentType")
    public JSONObject modifyPaymentType(@RequestBody JSONObject jsonObject, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
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


    @GetMapping("/selectPaymentType/{paymentTypeName}")
    public JSONObject selectPaymentType(@PathVariable("paymentTypeName") String  paymentTypeName, Authentication authentication){

        //检查权限
        try{
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        PaymentType paymentType = paymentTypeService.getPaymentTypeByName(paymentTypeName);

        //判断数据是否存在
        if (paymentType != null)
            return CommonUtil.errorJson(ErrorEnum.E_606);

        return CommonUtil.successJson(paymentType);
    }
}
