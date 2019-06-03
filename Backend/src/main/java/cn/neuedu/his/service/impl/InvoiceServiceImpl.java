package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.InvoiceMapper;
import cn.neuedu.his.model.Invoice;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.service.InvoiceService;
import cn.neuedu.his.service.PaymentService;
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
    private PaymentService paymentService;
    @Autowired
    private RedisServiceImpl redisService;

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
    public Integer addInvoiceByPayment(Payment payment) throws IllegalArgumentException, IndexOutOfBoundsException{
        if (payment.equals(null))
            throw new IllegalArgumentException("paymentId");
        Invoice invoice = new Invoice();
        Integer invoiceId;
        //从redis取出发票号
        try {
            invoiceId = redisService.getInvoiceSerialsNumberFromFront();
        }catch (IllegalArgumentException e) {
            throw new IndexOutOfBoundsException();
        }

        invoice.setId(invoiceId);
        invoice.setPriceAmount(payment.getUnitPrice().multiply(new BigDecimal(payment.getQuantity())));
        invoice.setCreatedDate(new Date(System.currentTimeMillis()));
        save(invoice);

        //更改缴费单发票id字段
        payment.setInvoiceId(invoiceId);
        paymentService.update(payment);

        return invoiceId;
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
        Integer invoiceId;
        //从redis取出发票号
        try {
            invoiceId = redisService.getInvoiceSerialsNumberFromFront();
        }catch (IllegalArgumentException e) {
            throw new IndexOutOfBoundsException();
        }

        invoice.setId(invoiceId);
        invoice.setCreatedDate(new Date(System.currentTimeMillis()));

        if (paymentIdList.isEmpty())
            throw new NullPointerException("paymentList");
        BigDecimal totalAmount = new BigDecimal(0);
        for (Integer paymentId: paymentIdList) {
            Payment payment = paymentService.findById(paymentId);
            if (payment == null)
                throw new IllegalArgumentException("paymentId");
            //更新payment中invoiceId属性
            payment.setInvoiceId(invoiceId);
            paymentService.update(payment);
            //计算总价
            totalAmount = totalAmount.add(payment.getUnitPrice().multiply(new BigDecimal(payment.getQuantity())));
        }
        invoice.setPriceAmount(totalAmount);

        save(invoice);
        return invoiceId;
    }

    /**
     * 设置发票号段到redis
     * @param start
     * @param end
     * @throws IllegalArgumentException
     */
    @Override
    public void setInvoiceNumberToRedis(Integer start, Integer end) throws IllegalArgumentException{
        try {
            redisService.setInvoiceSerialsNumberList(start, end);
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }


    @Override
    public Invoice getInvoiceAndPaymentByInvoiceId(Integer invoiceId) {
        return invoiceMapper.getInvoiceAndPaymentByInvoiceId(invoiceId);
    }
}
