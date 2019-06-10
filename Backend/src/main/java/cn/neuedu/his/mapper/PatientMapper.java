package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Patient;
import cn.neuedu.his.util.inter.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface PatientMapper extends MyMapper<Patient> {
    Patient getPatientAndAllPayment(@Param("id") Integer id, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
    Patient getPatientAndPaymentByState(@Param("patientId") Integer patientId, @Param("state") Integer state);
    Patient getPatientAndNotConsumePayment(@Param("patientId") Integer patientId, @Param("noPaymentTotalTypeId") Integer noPaymentTotalTypeId);
    Patient getPatientAndDrugByTypeAndState(@Param("patientId") Integer patientId, @Param("paymentTotalTypeId") Integer paymentTotalTypeId, @Param("state") Integer state, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //通过身份证号模糊查询获得病人
    List<Patient> selectPatientByIdentifyId(String id);
    //根据真实姓名模糊查询获得病人
    List<Patient> selectPatientByName(String name);
    //根据电话获得病人信息
    List<Patient> selectPatientByPhone(String phoneNumber);

    List<Patient> selectPatientByIdentifyIdAndNameAndPhone(String identifyId,String name, String phone);
}