package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.PaymentMapper;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.model.Registration;
import cn.neuedu.his.service.PaymentService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

import static cn.neuedu.his.util.constants.Constants.REGISTRATION_PAYMENT_TYPE;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class PaymentServiceImpl extends AbstractService<Payment> implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    /**
     * 生成缴费单
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
        payment.setHaveCompleted(true);
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
        newPayment.setIsRetreat(true);
        newPayment.setUnitPrice(originalPayment.getUnitPrice());
        newPayment.setOperatorId(registrationId);
        newPayment.setSettlementTypeId(originalPayment.getSettlementTypeId());
        newPayment.setPaymentTypeId(originalPayment.getPaymentTypeId());
        newPayment.setItemId(originalPayment.getItemId());
        newPayment.setCreateTime(new Date(System.currentTimeMillis()));
        newPayment.setPatientId(originalPayment.getPatientId());
        newPayment.setOperatorId(registrarId);


        if (newPayment.getPaymentTypeId().equals(REGISTRATION_PAYMENT_TYPE))
            newPayment.setHaveCompleted(true);

        save(newPayment);

        return newPayment.getId();
    }

    @Override
    public Payment findByRegistrationId(Integer registrationId) {
        return paymentMapper.selectByRegistrationId(registrationId, REGISTRATION_PAYMENT_TYPE);
    }
}
