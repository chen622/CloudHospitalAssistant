package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DrugMapper;
import cn.neuedu.his.model.Drug;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.service.DrugService;
import cn.neuedu.his.service.PaymentService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cn.neuedu.his.util.constants.Constants.*;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class DrugServiceImpl extends AbstractService<Drug> implements DrugService {

    @Autowired
    private DrugMapper drugMapper;
    @Autowired
    private PaymentService paymentService;

    /**
     * 取药
     * @param paymentId
     * @param drugId
     * @param drugOperatorId
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     */
    @Transactional
    @Override
    public void takeDrug(Integer paymentId, Integer drugId, Integer drugOperatorId) throws IllegalArgumentException, UnsupportedOperationException{
        Drug drug = findById(drugId);
        if (drug == null)
            throw new IllegalArgumentException("drugId");
        Payment payment = paymentService.findById(paymentId);
        if (payment == null)
            throw new IllegalArgumentException("paymentId");
        if (!payment.getState().equals(HAVE_PAID))
            throw new UnsupportedOperationException();
        drug.setStockAmount(drug.getStockAmount() - payment.getQuantity());
        update(drug);
        payment.setState(HAVE_COMPLETED_PAID);
        payment.setProjectOperatorId(drugOperatorId);
        paymentService.update(payment);
    }

    /**
     * 退药
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
    public void retreatDrug(Integer paymentId, Integer drugId, Integer quantity, Integer drugOperatorId) throws IllegalArgumentException, UnsupportedOperationException, IndexOutOfBoundsException{
        Drug drug = findById(drugId);
        if (drug == null)
            throw new IllegalArgumentException("drugId");

        drug.setStockAmount(drug.getStockAmount() + quantity);
        update(drug);

        try {
            paymentService.produceRetreatDrugPayment(paymentId, drugOperatorId, quantity);
        }catch (IllegalArgumentException e1) {
            throw new IllegalArgumentException(e1.getMessage());
        }catch (UnsupportedOperationException e2) {
            throw new UnsupportedOperationException(e2.getMessage());
        }catch (IndexOutOfBoundsException e3) {
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

        this.deleteById(id);
    }

    @Override
    public void modifyDrug(Drug drug) {
        //判断药物是否存在
        if (this.findById(drug.getId()) == null)
            throw new RuntimeException("626");

        //判断药品名是否重复
        if (this.getDrugByName(drug.getName()) != null)
            throw new RuntimeException("631");

        //判断药物类别是否正确
        if (DRUG_TYPE_LIST.contains(drug))
            throw new RuntimeException("627");

        //判断剂型是否正确
        if (drug.getFormulation()>LARGEST_FORMULATION ||drug.getFormulation()<LEAST_FORMULATION)
            throw new RuntimeException("628");

        this.update(drug);
    }

    @Override
    public void insertDrug(Drug drug) {
        //判断药品名是否重复
        if (this.getDrugByName(drug.getName()) != null)
            throw new RuntimeException("631");

        //判断药物类别是否正确
        if (DRUG_TYPE_LIST.contains(drug))
            throw new RuntimeException("627");

        //判断剂型是否正确
        if (drug.getFormulation()>1440 ||drug.getFormulation()<1401)
            throw new RuntimeException("628");

        this.save(drug);
    }
}
