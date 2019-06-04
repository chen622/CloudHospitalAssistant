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


    /**
     * 通过缴费信息，生成挂号发票
     * @param paymentId
     * @return
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException
     */
    @Override
    public Integer addInvoiceByPayment(Integer paymentId) throws IllegalArgumentException, IndexOutOfBoundsException{
        Payment payment = paymentService.findById(paymentId);
        if (payment == null)
            throw new IllegalArgumentException("paymentId");
        Invoice invoice = new Invoice();
        Integer invoiceId;
        //从redis取出发票号
        try {
            invoiceId = redisService.getInvoiceSerialsNumberFromFront();
        }catch (IllegalArgumentException e) {
            throw new IndexOutOfBoundsException();
        }
        //todo:invoiceId设为规律码（可以直接在model类进行处理）

        invoice.setId(invoiceId);
        invoice.setPriceAmount(payment.getUnitPrice().multiply(new BigDecimal(payment.getQuantity())));
        invoice.setCreatedDate(new Date(System.currentTimeMillis()));
        invoice.setOperatorId(payment.getOperatorId());
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
        invoice.setOperatorId(paymentService.findById(paymentIdList.get(0)).getOperatorId());

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
    public Invoice getInvoiceInfo(Integer invoiceId) throws  IllegalArgumentException{
        Invoice invoice = getInvoiceAndPaymentByInvoiceId(invoiceId);
        if (invoice == null)
            throw new IllegalArgumentException("invoiceId");

        return invoice;
    }

    /**
     * 发票重打
     * @param invoiceId
     * @throws IllegalArgumentException
     */
    @Override
    public void addAnewInvoice(Integer invoiceId, Integer admin) throws IllegalArgumentException {
        Invoice invoice = findById(invoiceId);
        if (invoice == null)
            throw new IllegalArgumentException("invoiceId");
        invoice.setAnewAmount(invoice.getAnewAmount() + 1);
        invoice.setOperatorId(admin);
        invoice.setCreatedDate(new Date(System.currentTimeMillis()));
        //todo:查看是否frozen
        update(invoice);
    }

    /**
     * 发票补打
     * @param invoiceId
     * @throws IllegalArgumentException
     */
    @Override
    public void addAgainInvoice(Integer invoiceId, Integer admin) throws IllegalArgumentException {
        Invoice invoice = findById(invoiceId);
        if (invoice == null)
            throw new IllegalArgumentException("invoiceId");
        invoice.setAgainAmount(invoice.getAgainAmount() + 1);
        invoice.setOperatorId(admin);
        invoice.setCreatedDate(new Date(System.currentTimeMillis()));
        update(invoice);
    }

    @Override
    public Invoice getInvoiceAndPaymentByInvoiceId(Integer invoiceId) {
        return invoiceMapper.getInvoiceAndPaymentByInvoiceId(invoiceId);
    }

    @Override
    public ArrayList<Integer> getInvoiceNormalIdList(Integer settleId) {
        return invoiceMapper.getInvoiceNormalList(settleId);
    }

    @Override
    public ArrayList<Integer> getInvoiceAnewIdList(Integer settleId) {
        return invoiceMapper.getInvoiceAnewList(settleId);
    }
}
