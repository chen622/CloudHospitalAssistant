package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.PaymentMapper;
import cn.neuedu.his.model.*;
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
import java.util.Map;

/**
 * Created by ccm on 2019/05/24.
 */
@Service
public class PaymentServiceImpl extends AbstractService<Payment> implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private PaymentTypeService paymentTypeService;
    @Autowired
    private RegistrationTypeService registrationTypeService;
    @Autowired
    private JobScheduleService jobScheduleService;
    @Autowired
    RedisServiceImpl redisService;

    @Override
    public List<Payment> getAll(Integer patientId, Date start, Date end) {
        List<Payment> payments = paymentMapper.getAllPaymentWithPaymentTypeByPatientId(patientId, start, end);
        if (payments == null) {
            payments = new ArrayList<>();
        }
        return payments;
    }



    @Override
    public List<Payment> getByDoctor(Integer patientId, Integer doctorId) {
        List<Payment> payments = paymentMapper.getAllPaymentWithPaymentTypeByDoctorIdAndPatientId(doctorId, patientId);
        if (payments == null) {
            payments = new ArrayList<>();
        }
        return payments;
    }

    /**
     * 生成挂号缴费单
     *
     * @param registrationId
     * @return
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException
     */
    @Override
    public Payment createRegistrationPayment(Integer registrationId) throws IllegalArgumentException, UnsupportedOperationException {
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
            payment.setUnitPrice(payment.getUnitPrice().add(new BigDecimal(1)));
        payment.setCreateTime(new Date(System.currentTimeMillis()));

        Map<String, Integer> map;
        try {
            map = redisService.getMapAll("paymentType");
        } catch (Exception e) {
            throw new UnsupportedOperationException("redis");
        }
        payment.setPaymentTypeId(map.get("挂号费"));

        payment.setState(Constants.PRODUCE_PAYMENT);
        payment.setDoctorId(registration.getDoctorId());
        save(payment);

        return payment;
    }

    /**
     * 支付挂号缴费单
     *
     * @param paymentId
     * @param settlementTypeId
     * @throws RuntimeException
     */
    @Override
    public Invoice payRegistrationPayment(Integer paymentId, Integer settlementTypeId) throws IllegalArgumentException, IndexOutOfBoundsException {
        Payment payment = findById(paymentId);
        if (payment == null)
            throw new IllegalArgumentException("paymentId");
        payment.setSettlementTypeId(settlementTypeId);
        payment.setState(Constants.HAVE_PAID);
        update(payment);

        //生成发票
        return invoiceService.addInvoiceByPayment(payment.getId());
    }

    /**
     * 缴费（包括检查项目和药费）
     *
     * @param paymentIdList
     * @param settlementTypeId
     * @param tollKeeperId
     * @return
     */
    @Override
    public JSONObject payPayment(ArrayList<Integer> paymentIdList, Integer settlementTypeId, Integer tollKeeperId) throws RuntimeException {
        ArrayList<Integer> successId = new ArrayList<>(); //成功的缴费单号
        ArrayList<Integer> failId = new ArrayList<>(); //失败的缴费单号

        //更新所有缴费单
        for (Integer paymentId : paymentIdList) {
            Payment payment = findById(paymentId);
            if (payment == null || !payment.getState().equals(Constants.PRODUCE_PAYMENT)) {
                failId.add(paymentId);
                continue;
            }

            payment.setOperatorId(tollKeeperId);
            payment.setState(Constants.HAVE_PAID);
            payment.setSettlementTypeId(settlementTypeId);

            update(payment);
            successId.add(paymentId);
        }

        JSONObject result = new JSONObject();
        try {
            if (!successId.isEmpty())
                //生成发票
                result.put("invoice",invoiceService.addInvoiceByPaymentList(successId));
        } catch (RuntimeException e) {
            throw new NullPointerException();
        }

        result.put("successId", successId);
        result.put("failId", failId);

        return result;
    }

    /**
     * 检验项目、挂号类，药品类（未取药）退费
     *
     * @param paymentId
     * @param adminId
     * @return
     */
    @Override
    public Invoice retreatPayment(Integer paymentId, Integer adminId, Integer retreatQuantity) throws IllegalArgumentException, UnsupportedOperationException, IndexOutOfBoundsException {
        //查找原缴费单
        Payment originalPayment = findById(paymentId);
        if (originalPayment == null)
            throw new IllegalArgumentException("paymentId");

        //确定是否能退:1.未冻结 2.处于缴费未使用或还未退药状态（状态均指完成缴费）
        Integer totalTypeId = paymentTypeService.getTotalPaymentType(originalPayment.getPaymentTypeId());
        if (originalPayment.getIsFrozen().equals(true) && !isAbleToRetreatState(originalPayment.getItemId(), totalTypeId, originalPayment.getState()))
            throw new UnsupportedOperationException("payment");

        //判断退费数量是否合法(药物）并改变原payment状态
        if (!isValidQuantity(originalPayment, retreatQuantity, totalTypeId))
            throw new IndexOutOfBoundsException();

        //填入新的信息
        Integer newPaymentId = addPayment(originalPayment, retreatQuantity, adminId);

        //生成冲红发票，若无法生成，抛出异常
        return invoiceService.addInvoiceByPayment(newPaymentId);
    }

    /**
     * 退还药物，生成缴费单
     *
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
        if (originalPayment == null)
            throw new IllegalArgumentException("paymentId");

        //如果不是药物，则抛出异常
        Integer totalTypeId = paymentTypeService.getTotalPaymentType(originalPayment.getPaymentTypeId());
        if (!totalTypeId.equals(Constants.DRUG_PAYMENT_TYPE))
            throw new UnsupportedOperationException("paymentType");

        //确定是否能退:1.未冻结 2.处于取完药状态
        if (originalPayment.getIsFrozen().equals(true) || !(originalPayment.getState().equals(Constants.HAVE_COMPLETED_PAID) || originalPayment.getState().equals(Constants.HAPPEN_RETREAT)))
            throw new UnsupportedOperationException("paymentState");

        //判断数量是否溢出并改变原payment状态
        if (!isValidQuantity(originalPayment, retreatQuantity, totalTypeId))
            throw new IndexOutOfBoundsException();

        //填入新的信息
        addPayment(originalPayment, retreatQuantity, adminId);
    }

    /**
     * 药品退费，生成发票
     *
     * @param paymentId
     * @param paymentAdminId
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     */
    @Override
    public Invoice retreatDrugFee(Integer paymentId, Integer paymentAdminId) throws IllegalArgumentException, UnsupportedOperationException {
        Payment payment = findById(paymentId);
        if (payment == null)
            throw new IllegalArgumentException();

        Integer totalTypeId = paymentTypeService.getTotalPaymentType(payment.getPaymentTypeId());
        if (!totalTypeId.equals(Constants.DRUG_PAYMENT_TYPE) || !payment.getState().equals(Constants.HAVE_RETURN_DRUG))
            throw new UnsupportedOperationException();

        payment.setState(Constants.HAVE_RETREAT);
        payment.setOperatorId(paymentAdminId);
        update(payment);

        return invoiceService.addInvoiceByPayment(payment.getId());
    }

    /**
     * 判断该payment是否可退
     *
     * @param itemId
     * @param totalTypeId
     * @param state
     * @return
     */
    private boolean isAbleToRetreatState(Integer itemId, Integer totalTypeId, Integer state) {
        if (totalTypeId.equals(Constants.REGISTRATION_PAYMENT_TYPE)) {
            if (registrationService.getRegistrationState(itemId).equals(Constants.WAITING_FOR_TREATMENT))
                return true;
        } else {
            if (state.equals(Constants.HAVE_PAID))
                return true;
        }
        return false;
    }

    /**
     * 确定数量是否合法, 并改变原payment状态
     *
     * @param originalPayment
     * @param retreatQuantity
     * @return
     */
    private Boolean isValidQuantity(Payment originalPayment, Integer retreatQuantity, Integer totalTypeId) {
        if (!totalTypeId.equals(Constants.DRUG_PAYMENT_TYPE)) {
            originalPayment.setState(Constants.HAPPEN_RETREAT_ALL);
            update(originalPayment);
            return true;
        }

        Integer currentRemainQuantity = 0;
        if (originalPayment.getState().equals(Constants.HAPPEN_RETREAT)) {
            //获取所有payment（包括冲红）
            ArrayList<Payment> paymentList = findAllByItemAndPaymentType(originalPayment.getItemId(), originalPayment.getPaymentTypeId());

            for (Payment payment : paymentList) {
                currentRemainQuantity = currentRemainQuantity + payment.getQuantity();
            }
        } else {
            currentRemainQuantity = originalPayment.getQuantity();
        }
        System.out.println(currentRemainQuantity);
        System.out.println(retreatQuantity);
        //判断数量是否合法，并改变原payment状态
        Integer remainQuantity = currentRemainQuantity - retreatQuantity;
        if (remainQuantity < 0)
            return false;
        else if (remainQuantity > 0)
            originalPayment.setState(Constants.HAPPEN_RETREAT);
        else
            originalPayment.setState(Constants.HAPPEN_RETREAT_ALL);
        update(originalPayment);

        return true;
    }

    /**
     * 生成新的退药缴费单
     *
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
        newPayment.setState(Constants.HAVE_RETURN_DRUG);
        newPayment.setQuantity(retreatQuantity * (-1));
        newPayment.setOperatorId(adminId);

        save(newPayment);

        return newPayment.getId();

    }


    @Override
    public Payment findByRegistrationId(Integer registrationId) {
        return paymentMapper.getByItemId(registrationId, Constants.REGISTRATION_PAYMENT_TYPE);
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

    /**
     * 根据item和缴费类型，找出所有相关payment
     *
     * @param itemId
     * @param paymentTypeId
     * @return
     */
    @Override
    public ArrayList<Payment> findAllByItemAndPaymentType(Integer itemId, Integer paymentTypeId) {
        return paymentMapper.getAllByItemIdAndPaymentTypeId(itemId, paymentTypeId);
    }

    /**
     * 根据item，缴费类型和状态，找出所有相关payment
     *
     * @param itemId
     * @param paymentTypeId
     * @param state
     * @return
     */
    @Override
    public ArrayList<Payment> findAllByItemAndPaymentTypeAndState(Integer itemId, Integer paymentTypeId, Integer state) {
        return paymentMapper.getAllByItemIdAndPaymentTypeIdAndState(itemId, paymentTypeId, state);
    }

    @Override
    public ArrayList<Payment> getForStatistics(Integer doctorId, Integer patientId, String start, String end) {
        ArrayList<Payment> list=paymentMapper.getForStatistics(doctorId, patientId, start, end);
        if(list==null){
            list=new ArrayList<>();
        }
        return list;
    }

}
