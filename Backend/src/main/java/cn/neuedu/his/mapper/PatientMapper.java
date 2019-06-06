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

    //通过身份证号模糊查询获得病人
    List<Patient> selectPatientByIdentifyId(String id);
    //根据真实姓名模糊查询获得病人
    List<Patient> selectPatientByName(String name);
    //根据电话获得病人信息
    List<Patient> selectPatientByPhone(String phoneNumber);
}