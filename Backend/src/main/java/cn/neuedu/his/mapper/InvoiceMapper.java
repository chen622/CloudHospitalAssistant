package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Invoice;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

@Component
public interface InvoiceMapper extends MyMapper<Invoice> {
    Invoice getInvoiceAndPaymentByInvoiceId(Integer invoiceId);
}