package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.InvoiceMapper;
import cn.neuedu.his.model.*;
import cn.neuedu.his.service.*;
import cn.neuedu.his.util.constants.Constants;
import cn.neuedu.his.util.inter.AbstractService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
    @Autowired
    private PaymentTypeService paymentTypeService;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private PrescriptionService prescriptionService;
    @Autowired
    private InspectionApplicationService inspectionApplicationService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private ConstantVariableService constantVariableService;


    /**
     * 通过缴费信息，生成挂号发票
     * @param paymentId
     * @return
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException
     */
    @Override
    public Invoice addInvoiceByPayment(Integer paymentId) throws IllegalArgumentException, IndexOutOfBoundsException{
        Payment payment = paymentService.findById(paymentId);
        if (payment == null)
            throw new IllegalArgumentException("paymentId");
        Invoice invoice = new Invoice();
        Integer invoiceId;
        //从redis取出发票号
        try {
            invoiceId = redisService.getInvoiceSerialsNumberFromFront();
        }catch (IllegalArgumentException e) {
            throw new IndexOutOfBoundsException("invoice");
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

        return invoice;
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

    /**
     * 获取发票模板信息
     * @param invoiceId
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public JSONObject getInvoiceInfo(Integer invoiceId) throws IllegalArgumentException{
        JSONObject result = new JSONObject();
        Invoice invoice = getInvoiceAndPaymentByInvoiceId(invoiceId);
        if (invoice == null)
            throw new IllegalArgumentException("invoiceId");
        if (invoice.getPaymentList().isEmpty())
            throw new IllegalArgumentException("paymentId");

        //根据发票各缴费类型，将缴费信息分条加入
        JSONArray jsonArray = new JSONArray();
        for (Payment payment: invoice.getPaymentList()) {
            PaymentType paymentType = paymentTypeService.findById(payment.getPaymentTypeId());
            String typeName = paymentType.getName();
            String projectName;
            String note = null;

            //挂号
            if (paymentType.getType().equals(Constants.REGISTRATION_PAYMENT_TYPE)) {
                Registration registration = registrationService.findRegistrationAndType(payment.getItemId());
                projectName = registration.getRegistrationType().getName();
                if (registration.getNeedBook().equals(true))
                    note = "需要病历本";
            //处方
            } else if (paymentType.getType().equals(Constants.DRUG_PAYMENT_TYPE)){
                projectName = prescriptionService.findPrescriptionAndDrug(payment.getItemId()).getDrug().getName();
            //检查项目
            } else {
                projectName = inspectionApplicationService.findInspectionAndNonDrug(payment.getItemId()).getNonDrug().getName();
            }
            //将明细信息加入
            jsonArray.add(putInvoiceContent(projectName, typeName, payment.getUnitPrice(), payment.getQuantity(), note));
        }

        //将信息存入result
        Payment paymentExample = invoice.getPaymentList().get(0);
        result.put("patient", patientService.findById(paymentExample.getPatientId()));
        result.put("settlementType", constantVariableService.findById(paymentExample.getSettlementTypeId()).getName());
        invoice.setPaymentList(null);
        result.put("invoice", invoice);
        result.put("item", jsonArray);

        return result;
    }

    /**
     * 发票上明细
     * @param name：项目名称
     * @param paymentType：缴费类型
     * @param unitPrice：单价
     * @param quantity：数量
     * @param note：备注
     * @return
     */
    private JSONObject putInvoiceContent(String name, String paymentType, BigDecimal unitPrice, Integer quantity, String note) {
        JSONObject result = new JSONObject();
        result.put("name", name);
        result.put("paymentType", paymentType);
        result.put("unitPrice", unitPrice);
        result.put("quantity", quantity);
        result.put("itemTotalAmount", unitPrice.add(new BigDecimal(quantity)));
        if (note == null)
            result.put("note", "");
        else
            result.put("note", note);
        return result;
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

    @Override
    public Integer getInvoiceNumberByAllDoctor(Integer projectOperatorId, Date start, Date end) {
        return invoiceMapper.getInvoiceNumberByAllDoctor(projectOperatorId, start, end);
    }
}
