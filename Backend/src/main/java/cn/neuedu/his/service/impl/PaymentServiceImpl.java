package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.PaymentMapper;
import cn.neuedu.his.model.Drug;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.model.Prescription;
import cn.neuedu.his.model.Registration;
import cn.neuedu.his.service.*;
import cn.neuedu.his.util.constants.Constants;
import cn.neuedu.his.util.inter.AbstractService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.neuedu.his.util.constants.Constants.*;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class PaymentServiceImpl extends AbstractService<Payment> implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;
    @Autowired
    private MedicalRecordService medicalRecordService;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private DrugService drugService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private PaymentTypeService paymentTypeService;
    @Autowired
    private RegistrationTypeService registrationTypeService;
    @Autowired
    private JobScheduleService jobScheduleService;

    /**
     * 生成挂号缴费单
     * @param registrationId
     * @return
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException
     */
    @Override
    public Integer createRegistrationPayment(Integer registrationId) throws IllegalArgumentException{
        Registration registration = registrationService.findById(registrationId);
        if (registration == null)
            throw new IllegalArgumentException("registrationId");

        //新增账单信息
        Payment payment = new Payment();
        payment.setItemId(registration.getId());
        payment.setPatientId(registration.getPatientId());
        payment.setOperatorId(registration.getRegistrarId());
        BigDecimal unitPrice = registrationTypeService.findById(jobScheduleService.findById(registration.getScheduleId()).getRegistrationTypeId()).getPrice();
        payment.setUnitPrice(unitPrice);
        if (registration.getNeedBook())
            payment.setUnitPrice(payment.getUnitPrice() .add(new BigDecimal(1)));
        payment.setCreateTime(new Date(System.currentTimeMillis()));
        payment.setPaymentTypeId(Constants.REGISTRATION_FEE_TYPE);
        payment.setState(Constants.PRODUCE_PAYMENT);
        payment.setDoctorId(registration.getDoctorId());
        save(payment);

        return payment.getId();
    }

    /**
     * 支付挂号缴费单
     * @param paymentId
     * @param settlementTypeId
     * @throws RuntimeException
     */
    @Override
    public void payRegistrationPayment(Integer paymentId, Integer settlementTypeId) throws IndexOutOfBoundsException {
        Payment payment = findById(paymentId);
        payment.setSettlementTypeId(settlementTypeId);
        payment.setState(Constants.HAVE_PAID);
        update(payment);

        //生成发票
        try {
            invoiceService.addInvoiceByPayment(payment.getId());
        }catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("invoice");
        }
    }

    /**
     * 缴费（包括检查项目和药费）
     * @param paymentIdList
     * @param settlementTypeId
     * @param tollKeeperId
     * @return
     */
    @Override
    public JSONObject payPayment(ArrayList<Integer> paymentIdList, Integer settlementTypeId, Integer tollKeeperId) throws RuntimeException{
        ArrayList successId = new ArrayList(); //成功的缴费单号
        ArrayList failId = new ArrayList(); //失败的缴费单号

        //更新所有缴费单
        for(Integer paymentId: paymentIdList) {
            Payment payment = findById(paymentId);
            if (payment == null || !payment.getState().equals(PRODUCE_PAYMENT)) {
                failId.add(paymentId);
                continue;
            }

            payment.setOperatorId(tollKeeperId);
            payment.setState(HAVE_PAID);
            payment.setSettlementTypeId(settlementTypeId);

            update(payment);
            successId.add(paymentId);
        }

        try {
            if (!successId.isEmpty())
                //生成发票
                invoiceService.addInvoiceByPaymentList(successId);
        }catch (RuntimeException e) {
            throw new NullPointerException();
        }

        JSONObject result = new JSONObject();
        result.put("successId", successId);
        result.put("failId", failId);

        return result;
    }

    /**
     * 检验项目、挂号类，药品类（未取药）退费
     * @param paymentId
     * @param adminId
     * @return
     */
    @Override
    public void retreatPayment(Integer paymentId, Integer adminId, Integer retreatQuantity) throws IllegalArgumentException, UnsupportedOperationException, IndexOutOfBoundsException{
        //查找原缴费单
        Payment originalPayment = findById(paymentId);
        if (originalPayment == null)
            throw new IllegalArgumentException("paymentId");
        //获取所有payment（包括冲红）
        ArrayList<Payment> paymentList = paymentMapper.selectAllByItemIdAndPaymentTypeId(originalPayment.getItemId(), originalPayment.getPaymentTypeId());

        //获取目前患者已检查（挂号）数量，若为0，则表示已退费，抛出异常
        Integer retreatIndex = 0;
        for (Payment payment: paymentList) {
            retreatIndex = retreatIndex + payment.getQuantity();
        }
        if (retreatIndex.equals(0))
            throw new IndexOutOfBoundsException();

        //确定是否能退:1.未冻结 2.处于缴费未使用或还未退药状态（状态均指完成缴费）
        if (originalPayment.getIsFrozen().equals(true) && !canRetreat(originalPayment.getItemId(), originalPayment.getPaymentTypeId(), originalPayment.getState()))
            throw new UnsupportedOperationException("payment");

        //填入新的信息
        Integer newPaymentId = addPayment(originalPayment, retreatQuantity, adminId);

        //生成冲红发票，若无法生成，抛出异常
        try {
            invoiceService.addInvoiceByPayment(newPaymentId);
        }catch (RuntimeException e) {
            throw new UnsupportedOperationException("invoice");
        }
    }

    /**
     * 退还药物，生成缴费单
     * @param paymentId
     * @param adminId
     * @param retreatQuantity
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     * @throws IndexOutOfBoundsException
     */
    @Override
    public void produceRetreatDrugPayment(Integer paymentId, Integer adminId, Integer retreatQuantity) throws IllegalArgumentException, UnsupportedOperationException, IndexOutOfBoundsException {
        //查找原缴费单
        Payment originalPayment = findById(paymentId);
        if(originalPayment == null)
            throw new IllegalArgumentException("paymentId");
        //如果不是药物，则抛出异常
        Integer totalTypeId = paymentTypeService.getTotalPaymentType(originalPayment.getPaymentTypeId());
        if (!totalTypeId.equals(DRUG_PAYMENT_TYPE ))
            throw new UnsupportedOperationException("paymentType");
        //获取所有payment（包括冲红）
        ArrayList<Payment> paymentList = paymentMapper.selectAllByItemIdAndPaymentTypeId(originalPayment.getItemId(), originalPayment.getPaymentTypeId());

        //获取目前患者手中药物数量
        Integer retreatIndex = 0;
        for (Payment payment: paymentList) {
            retreatIndex = retreatIndex + payment.getQuantity();
        }
        if (retreatQuantity > retreatIndex)
            throw new IndexOutOfBoundsException();

        //确定是否能退:1.未冻结 2.处于取完药状态
        if (originalPayment.getIsFrozen().equals(true) || !originalPayment.getState().equals(HAVE_COMPLETED_PAID))
            throw new UnsupportedOperationException("paymentState");

        //填入新的信息
        addPayment(originalPayment, retreatQuantity, adminId);
    }

    /**
     * 药品退费，生成发票
     * @param paymentId
     * @param paymentAdminId
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     */
    @Override
    public void retreatDrugFee(Integer paymentId, Integer paymentAdminId)throws IllegalArgumentException, UnsupportedOperationException{
        Payment payment = findById(paymentId);
        if (payment == null)
            throw new IllegalArgumentException();

        Integer totalTypeId = paymentTypeService.getTotalPaymentType(payment.getPaymentTypeId());
        if (!totalTypeId.equals(DRUG_PAYMENT_TYPE) || !payment.getState().equals(HAVE_RETURN_DRUG))
            throw new UnsupportedOperationException();

        payment.setState(HAVE_RETREAT);
        payment.setOperatorId(paymentAdminId);
        update(payment);

        invoiceService.addInvoiceByPayment(payment.getId());
    }

    /**
     * 生成新的退药缴费单
     * @param originalPayment
     * @param retreatQuantity
     * @param adminId
     * @return
     */
    private Integer addPayment(Payment originalPayment, Integer retreatQuantity, Integer adminId) {
        Payment newPayment = new Payment();
        newPayment.setUnitPrice(originalPayment.getUnitPrice());
        newPayment.setSettlementTypeId(originalPayment.getSettlementTypeId());
        newPayment.setPaymentTypeId(originalPayment.getPaymentTypeId());
        newPayment.setItemId(originalPayment.getItemId());
        newPayment.setCreateTime(new Date(System.currentTimeMillis()));
        newPayment.setPatientId(originalPayment.getPatientId());
        newPayment.setState(HAVE_RETURN_DRUG);
        newPayment.setQuantity(retreatQuantity * (-1));
        newPayment.setProjectOperatorId(adminId);

        save(newPayment);

        return newPayment.getId();

    }

    /**
     * 判断该payment是否可退
     * @param itemId
     * @param paymentType
     * @param state
     * @return
     */
    private boolean canRetreat(Integer itemId, Integer paymentType, Integer state) {
        if (paymentTypeService.getTotalPaymentType(paymentType).equals(REGISTRATION_PAYMENT_TYPE)) {
            if (registrationService.getRegistrationState(itemId).equals(Constants.WAITING_FOR_TREATMENT))
                return true;
        }else {
            if (state.equals(Constants.HAVE_PAID))
                return true;
        }
        return false;
    }






    /**
     * 通过处方创建缴费单
     * @param prescription
     * @return
     */
    @Override
    public Integer createDrugPayment(Prescription prescription) {
        Payment payment = new Payment();
        payment.setState(PRODUCE_PAYMENT);
        Registration registration = registrationService.findById(medicalRecordService.findById(prescription.getId()).getRegistrationId());
        payment.setOperatorId(registration.getDoctorId());
        payment.setPatientId(registration.getPatientId());
        payment.setCreateTime(new Date(System.currentTimeMillis()));
        payment.setItemId(prescription.getId());
        payment.setPaymentTypeId(DRUG_PAYMENT_TYPE);
        Drug drug = drugService.findById(prescription.getDrugId());
        payment.setPaymentTypeId(drug.getDrugType());
        payment.setUnitPrice(drug.getPrice());
        payment.setQuantity(prescription.getAmount());

        save(payment);
        return payment.getId();
    }





    @Override
    public Payment findByRegistrationId(Integer registrationId) {
        return paymentMapper.selectByItemId(registrationId, REGISTRATION_PAYMENT_TYPE);
    }

    @Override
    public void updateInvoiceId(Integer invoiceId, Integer id) {
        paymentMapper.updateInvoiceId(invoiceId, id);
    }

    @Override
    public ArrayList<Payment> getWithItem(Integer id) {
        return paymentMapper.getWithItem(id);
    }

    @Override
    public List<Payment> getByRegistrationId(Integer id, Integer type) {
        return paymentMapper.getByRegistrationId(id, type);
    }

    @Override
    public ArrayList<Payment> findByAllDoctor(Integer doctorId, Date start, Date end) {
        return paymentMapper.getByAllDoctor(doctorId, start, end);
    }

}
