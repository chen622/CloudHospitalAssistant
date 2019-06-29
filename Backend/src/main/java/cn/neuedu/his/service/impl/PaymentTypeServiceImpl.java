package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.PaymentTypeMapper;
import cn.neuedu.his.model.PaymentType;
import cn.neuedu.his.service.PaymentTypeService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ccm on 2019/05/24.
 */
@Service
public class PaymentTypeServiceImpl extends AbstractService<PaymentType> implements PaymentTypeService {

    @Autowired
    private PaymentTypeMapper paymentTypeMapper;
    @Autowired
    private RedisServiceImpl redisService;

    @Override
    public PaymentType getPaymentTypeByName(String paymentTypeName) {
        return paymentTypeMapper.getPaymentTypeByName(paymentTypeName);
    }

    /**
     * 通过二级缴费类型（西药费……）得出总缴费类型（处方费）
     *
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

    @Override
    public void insertPaymentType(PaymentType paymentType) {
        Map<String, Integer> payment = null;
        try {
            payment = redisService.getMapAll("paymentType");
        } catch (Exception e) {
            throw new RuntimeException("501.1");
        }
        //判断类型是否正确
        if (payment.values().contains(paymentType.getType()))
            throw new RuntimeException("501.2");
        //return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("结算类型"));

        try {
            this.save(paymentType);
            redisService.setPaymentType(paymentType);
        } catch (Exception e) {
            throw new RuntimeException("605");
            // return CommonUtil.errorJson(ErrorEnum.E_605);
        }
    }

    @Override
    public void deletePaymentType(Integer id) throws RuntimeException {
        PaymentType paymentType = this.findById(id);

        //检查结算类型是否存在
        if (paymentType == null)
            throw new RuntimeException("606");

        paymentType.setDelete(true);

        redisService.deletePaymentType(paymentType);
        this.update(paymentType);

    }

    @Override
    public void modifyPaymentType(PaymentType paymentType) throws RuntimeException {
        PaymentType lastPaymentType = this.getPaymentTypeByName(paymentType.getName());

        //判断数据是否存在
        if (lastPaymentType != null && lastPaymentType.getId() != paymentType.getId())
            throw new RuntimeException("605");
        //return CommonUtil.errorJson(ErrorEnum.E_606);

        redisService.setPaymentType(paymentType);
        this.update(paymentType);
    }

    @Override
    public ArrayList<PaymentType> getSmallPaymentType() {
        ArrayList<PaymentType> list = paymentTypeMapper.getSmallPaymentType();
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    @Override
    public List<PaymentType> getByTypeId(Integer typeId) {
        List<PaymentType> list = paymentTypeMapper.getByTypeId(typeId);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }
}
