package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Invoice;
import cn.neuedu.his.util.inter.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

@Component
public interface InvoiceMapper extends MyMapper<Invoice> {
    Invoice getInvoiceAndPaymentByInvoiceId(Integer invoiceId);
    ArrayList<Integer> getInvoiceNormalList(Integer settleId);
    ArrayList<Integer> getInvoiceAnewList(Integer settleId);
    Integer getInvoiceNumberByDoctorId(@Param("doctorId") Integer doctorId, @Param("startDate") Date startDate, @Param("endDate")Date endDate);
    Integer getInvoiceNumberByProjectOperatorId(@Param("projectOperatorId") Integer projectOperatorId, @Param("startDate") Date startDate, @Param("endDate")Date endDate);
}