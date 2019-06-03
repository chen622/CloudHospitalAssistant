package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.PaymentMapper;
import cn.neuedu.his.model.Drug;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.model.Prescription;
import cn.neuedu.his.model.Registration;
import cn.neuedu.his.service.*;
import cn.neuedu.his.util.inter.AbstractService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import static cn.neuedu.his.util.constants.Constants.*;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class PaymentServiceImpl extends AbstractService<Payment> implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;
    @Autowired
    private MedicalRecordService medicalRecordService;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private DrugService drugService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private PaymentTypeService paymentTypeService;

    /**
     * 生成挂号缴费单
     * @param registration
     * @param settlementTypeId
     * @param unitPrice
     * @return
     */
    @Override
    public Integer createRegistrationPayment(Registration registration, Integer settlementTypeId, BigDecimal unitPrice) {
        Payment payment = new Payment();

        payment.setItemId(registration.getId());
        payment.setPatientId(registration.getPatientId());
        payment.setOperatorId(registration.getRegistrarId());
        payment.setUnitPrice(unitPrice);
        if (registration.getNeedBook())
            payment.setUnitPrice(payment.getUnitPrice() .add(new BigDecimal(1)));
        payment.setSettlementTypeId(settlementTypeId);
        payment.setCreateTime(new Date(System.currentTimeMillis()));
        payment.setPaymentTypeId(REGISTRATION_FEE_TYPE);
        payment.setState(HAVE_PAID);
        save(payment);
        return payment.getId();
    }

    /**
     * 缴费（包括检查项目和药费）
     * @param paymentIdList
     * @param settlementTypeId
     * @param tollKeeperId
     * @return
     */
    @Override
    public JSONObject payPayment(ArrayList<Integer> paymentIdList, Integer settlementTypeId, Integer tollKeeperId) throws RuntimeException{
        ArrayList successId = new ArrayList(); //成功的缴费单号
        ArrayList failId = new ArrayList(); //失败的缴费单号

        //更新所有缴费单
        for(Integer paymentId: paymentIdList) {
            Payment payment = findById(paymentId);
            if (payment == null || !payment.getState().equals(PRODUCE_PAYMENT)) {
                failId.add(paymentId);
                continue;
            }

            payment.setOperatorId(tollKeeperId);
            payment.setState(HAVE_PAID);
            payment.setSettlementTypeId(settlementTypeId);

            update(payment);
            successId.add(paymentId);
        }

        try {
            if (!successId.isEmpty())
                //生成发票
                invoiceService.addInvoiceByPaymentList(successId);
        }catch (RuntimeException e) {
            throw new NullPointerException();
        }

        JSONObject result = new JSONObject();
        result.put("successId", successId);
        result.put("failId", failId);

        return result;
    }

    /**
     * 形成冲红缴费单（若是药物类，则处于退药不退钱状态）
     * @param paymentId
     * @param adminId
     * @param retreatQuantity -- 退回数量，主要针对drug，其余均设为1即可
     * @return
     */
    @Override
    public Integer produceRetreatPayment(Integer paymentId, Integer adminId, Integer retreatQuantity) throws IllegalArgumentException, UnsupportedOperationException, IndexOutOfBoundsException{
        //查找原缴费单
        Payment originalPayment = findById(paymentId);
        ArrayList<Payment> paymentList = paymentMapper.selectAllByItemIdAndPaymentTypeId(originalPayment.getItemId(), originalPayment.getPaymentTypeId());

        Integer retreatIndex = 0;
        for (Payment payment: paymentList) {
            retreatIndex = retreatIndex + payment.getQuantity();
        }

        if (originalPayment == null)
            throw new IllegalArgumentException("paymentId");

        //确定是否能退:1.未冻结 2.处于缴费未使用或还未退药状态（状态均指完成缴费）
        if (originalPayment.getIsFrozen().equals(true) || !originalPayment.getState().equals(HAVE_PAID))
            throw new UnsupportedOperationException("payment");

        //填入新的信息
        Payment newPayment = new Payment();
        newPayment.setUnitPrice(originalPayment.getUnitPrice());
        newPayment.setSettlementTypeId(originalPayment.getSettlementTypeId());
        newPayment.setPaymentTypeId(originalPayment.getPaymentTypeId());
        newPayment.setItemId(originalPayment.getItemId());
        newPayment.setCreateTime(new Date(System.currentTimeMillis()));
        newPayment.setPatientId(originalPayment.getPatientId());

        //药物与其他种类生成缴费单后状态有异，退药未退钱
        Integer totalTypeId = getTotalPaymentType(originalPayment.getPaymentTypeId());
        if (totalTypeId.equals(DRUG_PAYMENT_TYPE)) {
            newPayment.setState(HAVE_RETURN_DRUG);
            if (retreatQuantity > retreatIndex)
                throw new IndexOutOfBoundsException();
            newPayment.setQuantity(retreatQuantity * (-1));
            newPayment.setProjectOperatorId(adminId);
        }
        else {
            newPayment.setState(HAVE_RETREAT);
            if (retreatIndex.equals(0))
                throw new IndexOutOfBoundsException();
            newPayment.setQuantity(originalPayment.getQuantity() * (-1));
            newPayment.setOperatorId(adminId);
        }

        save(newPayment);

        //生成冲红发票，若无法生成，抛出异常
        try {
            if (!totalTypeId.equals(DRUG_PAYMENT_TYPE))
                invoiceService.addInvoiceByPayment(newPayment);
        }catch (RuntimeException e) {
            throw new UnsupportedOperationException("invoice");
        }

        return newPayment.getId();
    }

    /**
     * 药品退费，打印发票
     * @param paymentId
     * @param paymentAdminId
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     */
    @Override
    public void retreatDrugFee(Integer paymentId, Integer paymentAdminId)throws IllegalArgumentException, UnsupportedOperationException{
        Payment payment = findById(paymentId);
        if (payment == null)
            throw new IllegalArgumentException();

        Integer totalTypeId = getTotalPaymentType(payment.getPaymentTypeId());
        if (!totalTypeId.equals(DRUG_PAYMENT_TYPE) || !payment.getState().equals(HAVE_RETURN_DRUG))
            throw new UnsupportedOperationException();

        payment.setState(HAVE_RETREAT);
        payment.setOperatorId(paymentAdminId);
        update(payment);

        invoiceService.addInvoiceByPayment(payment);
    }






    /**
     * 通过处方创建缴费单
     * @param prescription
     * @return
     */
    @Override
    public Integer createDrugPayment(Prescription prescription) {
        Payment payment = new Payment();
        payment.setState(PRODUCE_PAYMENT);
        Registration registration = registrationService.findById(medicalRecordService.findById(prescription.getId()).getRegistrationId());
        payment.setOperatorId(registration.getDoctorId());
        payment.setPatientId(registration.getPatientId());
        payment.setCreateTime(new Date(System.currentTimeMillis()));
        payment.setItemId(prescription.getId());
        payment.setPaymentTypeId(DRUG_PAYMENT_TYPE);
        Drug drug = drugService.findById(prescription.getDrugId());
        payment.setPaymentTypeId(drug.getDrugType());
        payment.setUnitPrice(drug.getPrice());
        payment.setQuantity(prescription.getAmount());

        save(payment);
        return payment.getId();
    }





    @Override
    public Payment findByRegistrationId(Integer registrationId) {
        return paymentMapper.selectByItemId(registrationId, REGISTRATION_PAYMENT_TYPE);
    }

    @Override
    public void updateInvoiceId(Integer invoiceId, Integer id) {
        paymentMapper.updateInvoiceId(invoiceId, id);
    }

    /**
     * 通过二级缴费类型（西药费……）得出总缴费类型（处方费）
     * @param typeId
     * @return 总缴费类型
     */
    private Integer getTotalPaymentType(Integer typeId) {
        return paymentTypeService.findById(typeId).getType();
    }
}
