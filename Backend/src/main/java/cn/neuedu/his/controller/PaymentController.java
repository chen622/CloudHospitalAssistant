package cn.neuedu.his.controller;

import cn.neuedu.his.service.PaymentService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    /**
     * 挂号缴费
     * @param jsonObject
     * @param authentication
     * @return
     */
    @PostMapping("/payRegistration")
    public JSONObject payRegistration(@RequestBody JSONObject jsonObject, Authentication authentication) {
        try {
            PermissionCheck.getIdByPaymentAdmin(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }

        try {
            paymentService.payRegistrationPayment(jsonObject.getInteger("paymentId"), jsonObject.getInteger("settlementType"));
        }catch (IndexOutOfBoundsException e1) {
            return CommonUtil.errorJson(ErrorEnum.E_509);
        } catch (IllegalArgumentException e2) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e2.getMessage()));
        }

        return CommonUtil.successJson();
    }

    /**
     * 缴费
     * @param jsonObject
     * @param authentication
     * @return
     */
    @PostMapping("/pay")
    public JSONObject pay(@RequestBody JSONObject jsonObject, Authentication authentication) {
        Integer tollKeeper;
        try {
            tollKeeper = PermissionCheck.getIdByPaymentAdmin(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }

        JSONObject result;
        try {
            result = paymentService.payPayment((ArrayList<Integer>) jsonObject.getJSONArray("paymentIdList").toJavaList(Integer.class), jsonObject.getInteger("settlementType"), tollKeeper);
        }catch (RuntimeException e) {
            return CommonUtil.errorJson(ErrorEnum.E_505);
        }

        return CommonUtil.successJson(result);
    }

    /**
     * 检验项目、药品类（未取药）退费
     * @param jsonObject
     * @param authentication
     * @return
     */
    @PostMapping("/produceRetreatPayment")
    public JSONObject produceRetreatPayment(@RequestBody JSONObject jsonObject, Authentication authentication) {
        Integer tollKeeper;
        try {
            tollKeeper = PermissionCheck.getIdByAdminProducePayment(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }

        try {
            paymentService.retreatPayment(jsonObject.getInteger("paymentId"), tollKeeper, jsonObject.getInteger("quantity"));
        } catch (IllegalArgumentException e1) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e1.getMessage()));
        } catch (UnsupportedOperationException e2) {
            if (e2.getMessage().equals("payment"))
                return CommonUtil.errorJson(ErrorEnum.E_506);
            else if (e2.getMessage().equals("invoice"))
                return CommonUtil.errorJson(ErrorEnum.E_505);
        } catch (IndexOutOfBoundsException e3) {
            return CommonUtil.errorJson(ErrorEnum.E_507);
        }

        return CommonUtil.successJson();
    }

    /**
     * 药品类(已取药）退费
     * @param paymentId
     * @param authentication
     * @return
     */
    @PostMapping("/retreatDrugFee/{paymentId}")
    public JSONObject retreatDrugFee(@PathVariable("paymentId") Integer paymentId, Authentication authentication) {
        Integer tollKeeper;
        try {
            tollKeeper = PermissionCheck.getIdByPaymentAdmin(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }

        try {
            paymentService.retreatDrugFee(paymentId, tollKeeper);
        }catch (IllegalArgumentException e1) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e1.getMessage()));
        }catch (UnsupportedOperationException e2) {
            return CommonUtil.errorJson(ErrorEnum.E_506);
        }

        return CommonUtil.successJson();
    }
}
