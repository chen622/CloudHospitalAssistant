package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.PaymentTypeMapper;
import cn.neuedu.his.model.PaymentType;
import cn.neuedu.his.service.PaymentTypeService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class PaymentTypeServiceImpl extends AbstractService<PaymentType> implements PaymentTypeService {

    @Autowired
    private PaymentTypeMapper paymentTypeMapper;

    @Override
    public PaymentType getPaymentTypeByName(String paymentTypeName) {
        return paymentTypeMapper.getPaymentTypeByName(paymentTypeName);
    }
}
