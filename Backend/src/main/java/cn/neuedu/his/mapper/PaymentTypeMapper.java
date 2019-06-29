package cn.neuedu.his.mapper;

import cn.neuedu.his.model.PaymentType;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component


public interface PaymentTypeMapper extends MyMapper<PaymentType> {
    PaymentType getPaymentTypeByName(String paymentTypeName);
    ArrayList<PaymentType> getAllNotDelete();
    ArrayList<PaymentType> getSmallPaymentType();
    List<PaymentType> getByTypeId(Integer typeId);
}