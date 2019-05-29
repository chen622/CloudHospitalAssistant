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

    @Override
    public Invoice getInvoiceAndPaymentByInvoiceId(Integer invoiceId) {
        return invoiceMapper.getInvoiceAndPaymentByInvoiceId(invoiceId);
    }
}
