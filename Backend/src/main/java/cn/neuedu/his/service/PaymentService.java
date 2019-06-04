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
    Integer createRegistrationPayment(Integer registrationId, Integer settlementTypeId) throws IllegalArgumentException, IndexOutOfBoundsException;
    JSONObject payPayment(ArrayList<Integer> paymentIdList, Integer settlementTypeId, Integer tollKeeperId) throws RuntimeException;
    void retreatPayment(Integer paymentId, Integer adminId, Integer retreatQuantity) throws IllegalArgumentException, UnsupportedOperationException, IndexOutOfBoundsException;
    void produceRetreatDrugPayment(Integer paymentId, Integer adminId, Integer retreatQuantity) throws IllegalArgumentException, UnsupportedOperationException, IndexOutOfBoundsException;
    void retreatDrugFee(Integer paymentId, Integer paymentAdminId);


    Integer createDrugPayment(Prescription prescription);


    Payment findByRegistrationId(Integer registrationId);
    void updateInvoiceId(Integer invoiceId, Integer id);
}
