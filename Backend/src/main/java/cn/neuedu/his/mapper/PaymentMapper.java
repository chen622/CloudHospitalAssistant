package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Payment;
import cn.neuedu.his.util.inter.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public interface PaymentMapper extends MyMapper<Payment> {
    Payment getByItemId(@Param("registrationId") Integer registrationId, @Param("paymentTotalTypeId") Integer paymentTotalTypeId);

    ArrayList<Payment> getAllByItemIdAndPaymentTypeId(@Param("itemId") Integer itemId, @Param("paymentTypeId") Integer paymentTypeId);

    ArrayList<Payment> getWithItem(Integer id);

    List<Payment> getByRegistrationId(Integer id, Integer type);

    ArrayList<Payment> getByAllDoctor(@Param("projectOperatorId") Integer projectOperatorId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    ArrayList<Payment> getAllByItemIdAndPaymentTypeIdAndState(@Param("itemId") Integer itemId, @Param("paymentTypeId") Integer paymentTypeId, @Param("state") Integer state);

    ArrayList<Payment> getAllPaymentWithPaymentTypeByDoctorIdAndPatientId(Integer doctorId, Integer patientId);
}