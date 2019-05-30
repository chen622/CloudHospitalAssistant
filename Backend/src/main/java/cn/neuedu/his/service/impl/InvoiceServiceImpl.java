package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.InvoiceMapper;
import cn.neuedu.his.model.Invoice;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.service.InvoiceService;
import cn.neuedu.his.service.PaymentService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.constants.ErrorEnum;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class InvoiceServiceImpl extends AbstractService<Invoice> implements InvoiceService {

    @Autowired
    private InvoiceMapper invoiceMapper;
    @Autowired
    PaymentService paymentService;

    @Override
    public Invoice printInvoice(Integer invoiceId) throws  IllegalArgumentException{
        Invoice invoice = getInvoiceAndPaymentByInvoiceId(invoiceId);
        if (invoice == null)
            throw new IllegalArgumentException("invoiceId");

        return invoice;
    }

    /**
     * 通过缴费信息，生成挂号发票
     * @param payment
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Integer addInvoiceByPayment(Payment payment) throws IllegalArgumentException{
        if (payment.equals(null))
            throw new IllegalArgumentException("no payment");
        Invoice invoice = new Invoice();
        invoice.setPriceAmount(payment.getUnitPrice().multiply(new BigDecimal(payment.getQuantity())));
        invoice.setCreatedDate(new Date(System.currentTimeMillis()));
        save(invoice);

        //更改缴费单发票id字段
        payment.setInvoiceId(invoice.getId());
        paymentService.update(payment);

        return invoice.getId();
    }

    /**
     * 根据多条缴费单id，生成发票
     * 需要计算总额，并更改这些payment的invoiceId
     * @param paymentIdList
     * @return
     */
    @Override
    public Integer addInvoiceByPaymentList(ArrayList<Integer> paymentIdList) throws NullPointerException{
        Invoice invoice = new Invoice();
        invoice.setCreatedDate(new Date(System.currentTimeMillis()));

        ArrayList<Payment> paymentArrayList = new ArrayList<>();
        if (paymentArrayList.isEmpty())
            throw new NullPointerException("paymentList");
        BigDecimal totalAmount = new BigDecimal(0);
        for (Integer paymentId: paymentIdList) {
            Payment payment = paymentService.findById(paymentId);
            if (payment == null)
                throw new IllegalArgumentException("paymentId");
            totalAmount = totalAmount.add(payment.getUnitPrice().multiply(new BigDecimal(payment.getQuantity())));
            paymentArrayList.add(payment);
        }
        invoice.setPriceAmount(totalAmount);

        save(invoice);

        Integer invoiceId = invoice.getId();
        for (Payment payment: paymentArrayList) {
            payment.setInvoiceId(invoiceId);
            paymentService.update(payment);
        }

        return invoiceId;
    }

    @Override
    public Invoice getInvoiceAndPaymentByInvoiceId(Integer invoiceId) {
        return invoiceMapper.getInvoiceAndPaymentByInvoiceId(invoiceId);
    }
}
