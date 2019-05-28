package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Invoice;
import cn.neuedu.his.util.inter.MyMapper;


public interface InvoiceMapper extends MyMapper<Invoice> {
    Invoice getInvoiceAndPaymentByInvoiceId(Integer invoiceId);
}