package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Invoice;
import cn.neuedu.his.util.inter.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@Component
public interface InvoiceMapper extends MyMapper<Invoice> {
    Invoice getInvoiceAndPaymentByInvoiceId(Integer invoiceId);
    ArrayList<Integer> getInvoiceNormalList(Integer settleId);
    ArrayList<Integer> getInvoiceAnewList(Integer settleId);
    Integer getInvoiceNumberByDoctorId(@Param("doctorId") Integer doctorId, @Param("startDate") Date startDate, @Param("endDate")Date endDate);
    Integer getInvoiceNumberByAllDoctor(@Param("projectOperatorId") Integer projectOperatorId, @Param("startDate") Date startDate, @Param("endDate")Date endDate);
    Integer getInvoiceNumberByDepartment(@Param("doctorIdList") ArrayList<Integer> doctorIdList, @Param("startDate") Date startDate, @Param("endDate")Date endDate);
    BigDecimal getTotalFeeByType(@Param("type") Integer type, @Param("start") Date start, @Param("end")Date end);
    Integer getNormalInvoiceNumber(@Param("start") Date start, @Param("end")Date end);
    Integer getAnewInvoiceNumber(@Param("start") Date start, @Param("end")Date end);
    ArrayList<Invoice> getInvoiceAndPaymentByUser(@Param("userId") Integer userId, @Param("start") Date start, @Param("end")Date end);
    ArrayList<Invoice> getInvoiceInfoByDailySettle(@Param("dailySettleId") Integer dailySettleId);
}