package cn.neuedu.his.controller;

import cn.neuedu.his.model.Invoice;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.service.InvoiceService;
import cn.neuedu.his.service.PaymentService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.neuedu.his.util.PermissionCheck.canPrintInvoice;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    /**
     * 打印发票
     * @param invoiceId
     * @param authentication
     * @return
     */
    @GetMapping("/print/{invoiceId}")
    public JSONObject printVoice(@PathVariable("invoiceId") Integer invoiceId, Authentication authentication) {
        try {
            canPrintInvoice(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        try {
            return CommonUtil.successJson(invoiceService.printInvoice(invoiceId));
        }catch (IllegalArgumentException e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }
    }

}
