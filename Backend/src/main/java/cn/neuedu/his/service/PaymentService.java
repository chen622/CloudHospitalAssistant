package cn.neuedu.his.service;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.util.inter.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface PaymentService extends Service<Payment> {
    public void createRegistrationPayment(Integer registrationId, Integer settlementTypeId,
                                          Integer patientId, Integer unitPrice);
}
