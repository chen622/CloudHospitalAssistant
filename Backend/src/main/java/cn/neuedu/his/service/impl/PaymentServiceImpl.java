package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.PaymentMapper;
import cn.neuedu.his.model.Drug;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.model.Prescription;
import cn.neuedu.his.model.Registration;
import cn.neuedu.his.service.DrugService;
import cn.neuedu.his.service.MedicalRecordService;
import cn.neuedu.his.service.PaymentService;
import cn.neuedu.his.service.RegistrationService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        payment.setPaymentTypeId(REGISTRATION_PAYMENT_TYPE);
        payment.setState(HAVE_PAID);
        save(payment);
        return payment.getId();
    }

    /**
     * 形成冲红缴费单
     * @param registrationId
     * @param registrarId
     * @param retreatQuantity -- 退回数量，主要针对drug，其余均设为1即可
     * @return
     */
    @Override
    public Integer retreatPayment(Integer registrationId, Integer registrarId, Integer retreatQuantity) throws IllegalArgumentException{
        Payment originalPayment = findByRegistrationId(registrationId);
        if (originalPayment == null)
            throw new IllegalArgumentException();

        //填入新的信息
        Payment newPayment = new Payment();
        newPayment.setQuantity(retreatQuantity * (-1));
        newPayment.setUnitPrice(originalPayment.getUnitPrice());
        newPayment.setOperatorId(registrationId);
        newPayment.setSettlementTypeId(originalPayment.getSettlementTypeId());
        newPayment.setPaymentTypeId(originalPayment.getPaymentTypeId());
        newPayment.setItemId(originalPayment.getItemId());
        newPayment.setCreateTime(new Date(System.currentTimeMillis()));
        newPayment.setPatientId(originalPayment.getPatientId());
        newPayment.setOperatorId(registrarId);


        if (newPayment.getPaymentTypeId().equals(DRUG_PAYMENT_TYPE))
            newPayment.setState(HAVE_RETURN_DRUG);
        else
            newPayment.setState(HAVE_RETREAT);

        save(newPayment);

        return newPayment.getId();
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
}
