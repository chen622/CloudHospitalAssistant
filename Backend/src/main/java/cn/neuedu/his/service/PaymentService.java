package cn.neuedu.his.service;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.model.Registration;
import cn.neuedu.his.util.inter.Service;

import java.math.BigDecimal;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface PaymentService extends Service<Payment> {
    Integer createRegistrationPayment(Registration registration, Integer settlementTypeId, BigDecimal unitPrice);
    public Integer retreatPayment(Integer registrationId, Integer registrarId, Integer retreatQuantity) throws IllegalArgumentException;
    Payment findByRegistrationId(Integer registrationId);
}
