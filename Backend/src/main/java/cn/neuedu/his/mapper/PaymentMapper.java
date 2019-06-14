package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Payment;
import cn.neuedu.his.util.inter.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public interface PaymentMapper extends MyMapper<Payment> {
    Payment getByItemId(@Param("registrationId") Integer registrationId, @Param("paymentTotalTypeId") Integer paymentTotalTypeId);

    ArrayList<Payment> getAllByItemIdAndPaymentTypeId(@Param("itemId") Integer itemId, @Param("paymentTypeId") Integer paymentTypeId);

    ArrayList<Payment> getWithItem(Integer id);

    List<Payment> getByRegistrationId(Integer id, Integer type);

    ArrayList<Payment> getAllByDoctor(@Param("projectOperatorId") Integer projectOperatorId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    Integer getAllPayments(@Param("projectOperatorId") Integer projectOperatorId, @Param("startDate") String startDate, @Param("endDate") String endDate,Integer id);

    ArrayList<Payment> getAllByItemIdAndPaymentTypeIdAndState(@Param("itemId") Integer itemId, @Param("paymentTypeId") Integer paymentTypeId, @Param("state") Integer state);

    ArrayList<Payment> getAllPaymentWithPaymentTypeByDoctorIdAndPatientId(Integer doctorId, Integer patientId);

    ArrayList<Payment> getAllPaymentWithPaymentTypeByPatientId(Integer patientId, Date start, Date end);

    Map<Integer,Integer> getForStatistics(Integer doctorId, Integer patientId, String start, String end);

    Integer getStatistics(Integer patientId,String start,String end,Integer type);
}