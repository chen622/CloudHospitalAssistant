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

    @PostMapping("/pay")
    public JSONObject pay(@RequestBody JSONObject jsonObject, Authentication authentication) {
        Integer tollKeeper;
        try {
            tollKeeper = PermissionCheck.getIdByPaymentAdmin(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }
        JSONObject result = paymentService.payPayment((ArrayList<Integer>) jsonObject.getJSONArray("paymentIdList").toJavaList(Integer.class), jsonObject.getInteger("settlementType"), tollKeeper);

        return CommonUtil.successJson(result);
    }
}
