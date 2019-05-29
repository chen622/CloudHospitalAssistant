package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Payment;
import cn.neuedu.his.util.inter.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component

public interface PaymentMapper extends MyMapper<Payment> {
    Payment selectByItemId(@Param("registrationId") Integer registrationId, @Param("paymentTotalTypeId") Integer paymentTotalTypeId);
    void updateInvoiceId(@Param("invoiceId") Integer invoiceId, @Param("id") Integer id);
}