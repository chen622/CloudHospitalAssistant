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
    Integer addInvoiceByPayment(Payment payment) throws IllegalArgumentException, IndexOutOfBoundsException;
    Integer addInvoiceByPaymentList(ArrayList<Integer> paymentIdList);
    void setInvoiceNumberToRedis(Integer start, Integer end) throws IllegalArgumentException;
    Invoice printInvoice(Integer invoiceId) throws IllegalArgumentException;

    Invoice getInvoiceAndPaymentByInvoiceId(Integer invoiceId);
}
