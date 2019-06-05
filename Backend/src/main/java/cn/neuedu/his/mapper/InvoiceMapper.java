package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Invoice;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public interface InvoiceMapper extends MyMapper<Invoice> {
    Invoice getInvoiceAndPaymentByInvoiceId(Integer invoiceId);
    ArrayList<Integer> getInvoiceNormalList(Integer settleId);
    ArrayList<Integer> getInvoiceAnewList(Integer settleId);
}