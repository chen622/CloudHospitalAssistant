package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.PaymentTypeMapper;
import cn.neuedu.his.model.PaymentType;
import cn.neuedu.his.service.PaymentTypeService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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

    /**
     * 通过二级缴费类型（西药费……）得出总缴费类型（处方费）
     * @param typeId
     * @return 总缴费类型
     */
    @Override
    public Integer getTotalPaymentType(Integer typeId) {
        return findById(typeId).getType();
    }

    @Override
    public ArrayList<PaymentType> findAllNotDelete() {
        return paymentTypeMapper.getAllNotDelete();
    }
}
