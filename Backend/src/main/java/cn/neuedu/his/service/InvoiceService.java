package cn.neuedu.his.service;
import cn.neuedu.his.model.Invoice;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.util.inter.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface InvoiceService extends Service<Invoice> {
    Integer addInvoiceByPayment(Payment payment);
    Invoice getInvoiceAndPaymentByInvoiceId(Integer invoiceId);
    void printInvoice(Integer invoiceId) throws  IllegalArgumentException;
}
