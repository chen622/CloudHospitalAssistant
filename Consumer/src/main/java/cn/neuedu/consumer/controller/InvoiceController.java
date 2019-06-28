package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.InvoiceRemote;
import cn.neuedu.consumer.util.FeignRequestInterceptor;
import com.alibaba.fastjson.JSONObject;
import feign.Client;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoice")
@Import(FeignClientsConfiguration.class)
public class InvoiceController {
    private InvoiceRemote invoiceRemote;

    @Autowired
    public InvoiceController(
            Decoder decoder, Encoder encoder, Client client) {
        this.invoiceRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(InvoiceRemote.class, "http://eureka-producer");
    }

    @GetMapping("/print/{invoiceId}")
    public JSONObject printInvoice(@PathVariable("invoiceId") Integer invoiceId) {
        return invoiceRemote.printInvoice(invoiceId);
    }

    
    @PostMapping("/setPhase/{start}/{end}")
    public JSONObject setInvoiceNumberPhase(@PathVariable("start") Integer start, @PathVariable("end") Integer end) {
        return invoiceRemote.setInvoiceNumberPhase(start, end);
    }
    
    @PostMapping("/anewInvoice/{invoiceId}")
    public JSONObject anewInvoice(@PathVariable("invoiceId") Integer invoiceId) {
        return invoiceRemote.anewInvoice(invoiceId);
    }

    
    @PostMapping("/againInvoice/{invoiceId}")
    public JSONObject againInvoice(@PathVariable("invoiceId") Integer invoiceId) {
        return invoiceRemote.againInvoice(invoiceId);
    }

}
