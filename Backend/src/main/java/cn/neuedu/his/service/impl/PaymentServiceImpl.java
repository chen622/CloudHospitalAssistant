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
        return save(payment);
    }
}
