package cn.neuedu.his.service;
import cn.neuedu.his.model.PaymentType;
import cn.neuedu.his.util.inter.Service;

import java.util.ArrayList;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface PaymentTypeService extends Service<PaymentType> {
    PaymentType getPaymentTypeByName(String paymentTypeName);
    Integer getTotalPaymentType(Integer typeId);
    ArrayList<PaymentType> findAllNotDelete();

    void insertPaymentType(PaymentType paymentType);//插入结算类型
}
