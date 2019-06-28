package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

public interface InvoiceRemote {
    @GetMapping("/invoice/print/{invoiceId}")
    JSONObject printInvoice(@PathVariable("invoiceId") Integer invoiceId);


    @PostMapping("/invoice/setPhase/{start}/{end}")
    JSONObject setInvoiceNumberPhase(@PathVariable("start") Integer start, @PathVariable("end") Integer end);

    @PostMapping("/invoice/anewInvoice/{invoiceId}")
    JSONObject anewInvoice(@PathVariable("invoiceId") Integer invoiceId);


    @PostMapping("/invoice/againInvoice/{invoiceId}")
    JSONObject againInvoice(@PathVariable("invoiceId") Integer invoiceId);
}
