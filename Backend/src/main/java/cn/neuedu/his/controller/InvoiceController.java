package cn.neuedu.his.controller;

import cn.neuedu.his.service.InvoiceService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

//    /**
//     * 打印发票
//     * @param invoiceId
//     * @param authentication
//     * @return
//     */
//    @GetMapping("/print/{invoiceId}")
//    public JSONObject printVoice(@PathVariable("invoiceId") Integer invoiceId, Authentication authentication) {
//        try {
//            PermissionCheck.getIdByPaymentAdmin(authentication);
//        }catch (AuthenticationServiceException e) {
//            return CommonUtil.errorJson(ErrorEnum.E_502);
//        } catch (Exception e) {
//            return CommonUtil.errorJson(ErrorEnum.E_802);
//        }
//
//        try {
//            return CommonUtil.successJson(invoiceService.getInvoiceInfo(invoiceId));
//        }catch (IllegalArgumentException e) {
//            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
//        }
//    }

    /**
     * 将发票字段存入redis中
     * @param start
     * @param end
     * @return
     */
    @PostMapping("/setPhase/{start}/{end}")
    public JSONObject setInvoiceNumberPhase(@PathVariable("start") Integer start, @PathVariable("end") Integer end) {
        try {
            invoiceService.setInvoiceNumberToRedis(start, end);
        }catch (IllegalArgumentException e) {
            return CommonUtil.errorJson(ErrorEnum.E_508);
        }

        return CommonUtil.successJson();
    }

    /**
     * 重打发票
     * @param invoiceId
     * @param authentication
     * @return
     */
    @PostMapping("/anewInvoice/{invoiceId")
    public JSONObject anewInvoice(@PathVariable("invoiceId") Integer invoiceId, Authentication authentication) {
        Integer admin;
        try {
            admin = PermissionCheck.getIdByPaymentAdmin(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }

        try {
            invoiceService.addAnewInvoice(invoiceId, admin);
        }catch (IllegalArgumentException e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }

        return CommonUtil.successJson();
    }

    /**
     * 补打发票
     * @param invoiceId
     * @param authentication
     * @return
     */
    @PostMapping("/againInvoice/{invoiceId")
    public JSONObject againInvoice(@PathVariable("invoiceId") Integer invoiceId, Authentication authentication) {
        Integer admin;
        try {
            admin = PermissionCheck.getIdByPaymentAdmin(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }

        try {
            invoiceService.addAgainInvoice(invoiceId, admin);
        }catch (IllegalArgumentException e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }

        return CommonUtil.successJson();
    }

}
