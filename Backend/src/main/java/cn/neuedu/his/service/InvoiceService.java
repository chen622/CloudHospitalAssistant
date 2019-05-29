package cn.neuedu.his.service;
import cn.neuedu.his.model.Invoice;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.util.inter.Service;

import java.util.ArrayList;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface InvoiceService extends Service<Invoice> {
    Integer addInvoiceByPayment(Payment payment);
    Integer addInvoiceByPaymentList(ArrayList<Integer> paymentIdList);
    Invoice getInvoiceAndPaymentByInvoiceId(Integer invoiceId);
    Invoice printInvoice(Integer invoiceId) throws IllegalArgumentException;
}
