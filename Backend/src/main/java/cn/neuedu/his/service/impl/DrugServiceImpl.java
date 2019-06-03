package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DrugMapper;
import cn.neuedu.his.model.Drug;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.service.DrugService;
import cn.neuedu.his.service.PaymentService;
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

    @Transactional
    @Override
    public void retreatDrug(Integer paymentId, Integer drugId, Integer quantity, Integer drugOperatorId) throws IllegalArgumentException, UnsupportedOperationException, IndexOutOfBoundsException{
        Drug drug = findById(drugId);
        if (drug == null)
            throw new IllegalArgumentException("drugId");

        drug.setStockAmount(drug.getStockAmount() + quantity);
        update(drug);

        try {
            paymentService.produceRetreatPayment(paymentId, drugOperatorId, quantity);
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
}
