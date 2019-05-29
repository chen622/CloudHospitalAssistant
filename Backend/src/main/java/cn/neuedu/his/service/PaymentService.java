package cn.neuedu.his.service;
import cn.neuedu.his.model.Drug;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.model.Prescription;
import cn.neuedu.his.model.Registration;
import cn.neuedu.his.util.inter.Service;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface PaymentService extends Service<Payment> {
    Integer createRegistrationPayment(Registration registration, Integer settlementTypeId, BigDecimal unitPrice);
    Integer retreatPayment(Integer registrationId, Integer registrarId, Integer retreatQuantity) throws IllegalArgumentException;
    JSONObject payPayment(ArrayList<Integer> paymentIdList, Integer settlementTypeId, Integer tollKeeperId);


    Integer createDrugPayment(Prescription prescription);


    Payment findByRegistrationId(Integer registrationId);
    void updateInvoiceId(Integer invoiceId, Integer id);
}
