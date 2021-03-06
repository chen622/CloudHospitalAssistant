package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DrugMapper;
import cn.neuedu.his.model.ConstantVariable;
import cn.neuedu.his.model.Drug;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.service.DrugService;
import cn.neuedu.his.service.PaymentService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.LockUtil;
import cn.neuedu.his.util.constants.ErrorEnum;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.neuedu.his.util.constants.Constants.*;

/**
 * Created by ccm on 2019/05/24.
 */
@Service
public class DrugServiceImpl extends AbstractService<Drug> implements DrugService {

    @Autowired
    RedisServiceImpl redisService;
    @Autowired
    private DrugMapper drugMapper;
    @Autowired
    private PaymentService paymentService;

    private String drugKey = "-drug-lock";

    /**
     * 取药
     *
     * @param paymentId
     * @param drugId
     * @param drugOperatorId
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     */
    @Transactional
    @Override
    public void takeDrug(Integer paymentId, Integer drugId, Integer drugOperatorId) throws IllegalArgumentException, UnsupportedOperationException {
        Drug drug = findById(drugId);
        if (drug == null)
            throw new IllegalArgumentException("drugId");
        Payment payment = paymentService.findById(paymentId);
        if (payment == null)
            throw new IllegalArgumentException("paymentId");
        if (!payment.getState().equals(HAVE_PAID))
            throw new UnsupportedOperationException("paymentState");

        //设置锁
        boolean lock = LockUtil.lock(drugId.toString() + drugKey, 5000);
        if (lock) {
            try {
                //领取操作
                drug.setStockAmount(drug.getStockAmount() - payment.getQuantity());
                update(drug);
            } finally {
                //释放锁
                LockUtil.unLock(drugId.toString() + drugKey);
            }
        } else {
            throw new UnsupportedOperationException("locking");
        }

        payment.setState(HAVE_COMPLETED_PAID);
        payment.setProjectOperatorId(drugOperatorId);
        paymentService.update(payment);
    }

    /**
     * 退药
     *
     * @param paymentId
     * @param drugId
     * @param quantity
     * @param drugOperatorId
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     * @throws IndexOutOfBoundsException
     */
    @Transactional
    @Override
    public void retreatDrug(Integer paymentId, Integer drugId, Integer quantity, Integer drugOperatorId) throws IllegalArgumentException, UnsupportedOperationException, IndexOutOfBoundsException {
        Drug drug = findById(drugId);
        if (drug == null)
            throw new IllegalArgumentException("drugId");

        boolean lock = LockUtil.lock(drugId.toString() + drugKey, 5);
        if (lock) {
            try {
                //领取操作
                drug.setStockAmount(drug.getStockAmount() + quantity);
                update(drug);
            } finally {
                //释放锁
                LockUtil.unLock(drugId.toString() + drugKey);
            }
        } else {
            throw new UnsupportedOperationException("locking");
        }

        try {
            paymentService.produceRetreatDrugPayment(paymentId, drugOperatorId, quantity);
        } catch (IllegalArgumentException e1) {
            throw new IllegalArgumentException(e1.getMessage());
        } catch (UnsupportedOperationException e2) {
            throw new UnsupportedOperationException(e2.getMessage());
        } catch (IndexOutOfBoundsException e3) {
            throw new IndexOutOfBoundsException();
        }

    }

    @Override
    public List<Drug> getDrugByName(String name) {
        return drugMapper.getDrugByName(name);
    }

    @Override
    public void deleteDrug(Integer id) {
        Drug drug = this.findById(id);

        //判断药物是否存在
        if (drug == null)
            throw new RuntimeException("626");
        //return CommonUtil.errorJson(ErrorEnum.E_626);

        drug.setDelete(true);
        this.update(drug);
    }

    @Override
    public void modifyDrug(Drug drug) throws Exception {
        //判断药物是否存在
        if (this.findById(drug.getId()) == null)
            throw new RuntimeException("626");

        if (judgeDrug(drug))
            this.update(drug);

    }

    @Override
    public Integer insertDrug(Drug drug) throws Exception {
        //判断药品名是否重复
        if (this.getDrugByName(drug.getName()).size() > 0)
            throw new RuntimeException("631");
        if (judgeDrug(drug))
            this.save(drug);
        return drug.getId();
    }

    @Override
    public ArrayList<Drug> getAllDrug() {
        return drugMapper.getAllDrug();
    }

    @Override
    public ArrayList<Drug> getDrugByPartName(String name) {
        return drugMapper.getDrugByPartName(name);
    }

    @Override
    public List<ConstantVariable> getTypeAndDrugs(Boolean auth) {
        return drugMapper.getTypeAndDrugs(auth);
    }

    private boolean judgeDrug(Drug drug) throws Exception {


        Map<String, Integer> map = redisService.getMapAll("drugType");
        //判断药物类别是否正确
        if (!map.values().contains(drug.getDrugType().intValue()))
            throw new RuntimeException("627");


        Map<String, Integer> map2 = redisService.getMapAll("formulation");
        //判断剂型是否正确
        if (!map2.values().contains(drug.getFormulation()))
            throw new RuntimeException("628");
        return true;
    }
}
