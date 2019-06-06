package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Patient;
import cn.neuedu.his.util.inter.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface PatientMapper extends MyMapper<Patient> {
    Patient searchPatientAndFrozenPayment(@Param("patientId") Integer patientId, @Param("noPaymentTotalTypeId") Integer noPaymentTotalTypeId, @Param("state") Integer state);
    Patient searchPatientAndNotConsumePayment(@Param("patientId") Integer patientId, @Param("noPaymentTotalTypeId") Integer noPaymentTotalTypeId, @Param("state1") Integer state1, @Param("state2") Integer state2, @Param("state3") Integer state3);
    Patient searchPatientAndNotTakeDrug(@Param("patientId") Integer patientId, @Param("paymentTotalTypeId") Integer paymentTotalTypeId, @Param("state") Integer state);
    Patient searchPatientAndDrugDuringDate(@Param("patientId") Integer patientId, @Param("paymentTotalTypeId") Integer paymentTotalTypeId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //模糊查询获得病人Id
    List<Patient> selectPatientByIdentifyId(Integer id);
}