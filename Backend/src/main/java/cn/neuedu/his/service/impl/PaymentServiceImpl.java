package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.PaymentMapper;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.model.Registration;
import cn.neuedu.his.service.PaymentService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class PaymentServiceImpl extends AbstractService<Payment> implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public void createRegistrationPayment(Integer registrationId, Integer settlementTypeId, Integer patientId, Integer unitPrice) {
        Payment payment = new Payment();
        payment.setCreateTime(new Date(System.currentTimeMillis()));
        payment.set
    }
}
