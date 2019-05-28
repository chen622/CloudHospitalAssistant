package cn.neuedu.his.controller;

import cn.neuedu.his.model.Invoice;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.service.InvoiceService;
import cn.neuedu.his.service.PaymentService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/print/{invoiceId}")
    public JSONObject printVoice(@PathVariable("invoiceId") Integer invoiceId, Authentication authentication) {
        canPrintInvoice(authentication);
        Invoice invoice = invoiceService.getInvoiceAndPaymentByInvoiceId(invoiceId);
        if (invoice == null)
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("invoiceId"));
//        System.out.println(invoice.getId());
//        for (Payment payment : invoice.getPaymentList()) {
//            System.out.println(payment.getId());
//        }

        //TODO 打印成文件
        return CommonUtil.successJson();
    }

}
