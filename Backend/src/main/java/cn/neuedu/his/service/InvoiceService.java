package cn.neuedu.his.service;
import cn.neuedu.his.model.Invoice;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.util.inter.Service;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface InvoiceService extends Service<Invoice> {
    Invoice addInvoiceByPayment(Integer paymentId) throws IllegalArgumentException, IndexOutOfBoundsException;
    Invoice addInvoiceByPaymentList(ArrayList<Integer> paymentIdList);
    void setInvoiceNumberToRedis(Integer start, Integer end) throws IllegalArgumentException;
//    Invoice getInvoiceInfo(Integer invoiceId) throws IllegalArgumentException;
    void addAnewInvoice(Integer invoiceId, Integer admin) throws IllegalArgumentException;
    void addAgainInvoice(Integer invoiceId, Integer admin) throws IllegalArgumentException;
    JSONObject getInvoiceInfo(Integer invoiceId) throws IllegalArgumentException;

    Invoice getInvoiceAndPaymentByInvoiceId(Integer invoiceId);
    ArrayList<Integer> getInvoiceNormalIdList(Integer settleId);
    ArrayList<Integer> getInvoiceAnewIdList(Integer settleId);
    Integer getInvoiceNumberByAllDoctor(Integer doctorId, Date start, Date end);
}
