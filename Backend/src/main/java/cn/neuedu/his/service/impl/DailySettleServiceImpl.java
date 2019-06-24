package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DailySettleMapper;
import cn.neuedu.his.model.DailySettle;
import cn.neuedu.his.model.Invoice;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.service.*;
import cn.neuedu.his.util.constants.Constants;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ccm on 2019/06/04.
 */
@Service
public class DailySettleServiceImpl extends AbstractService<DailySettle> implements DailySettleService {

    @Autowired
    private DailySettleMapper dailySettleMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private PaymentTypeService paymentTypeService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private InvoiceService invoiceService;

    /**
     * 生成日结单信息
     * @param makeId
     * @param adminId
     * @param end
     * @return
     * @throws IndexOutOfBoundsException
     */
    public DailySettle produceSettleInfo(Integer makeId, Integer adminId, Date end) throws IndexOutOfBoundsException{
        //确定起止时间
        Date startDate = lastSettleDate(adminId);
        Date endDate = endSettleDate(end);

        //存储日结基本信息
        DailySettle dailySettle = new DailySettle();
        dailySettle.setAdminId(adminId);
        dailySettle.setMakeId(makeId);
        dailySettle.setMakeDate(new Date(System.currentTimeMillis()));
        dailySettle.setStartDate(startDate);
        dailySettle.setEndDate(endDate);

        //各类费用统计
        BigDecimal drugFee = invoiceService.getTotalFeeByType(Constants.DRUG_PAYMENT_TYPE, startDate, endDate);
        BigDecimal inspectionFee = invoiceService.getTotalFeeByType(Constants.NON_DRUG_PAYMENT_TYPE, startDate, endDate);
        BigDecimal registrationFee = invoiceService.getTotalFeeByType(Constants.REGISTRATION_PAYMENT_TYPE, startDate, endDate);
        BigDecimal dealFee = invoiceService.getTotalFeeByType(Constants.DEAL_PAYMENT_TYPE, startDate, endDate);
        dailySettle.setTotalFee(drugFee.add(inspectionFee).add(registrationFee));

        dailySettle.setDrugFee(drugFee);
        dailySettle.setInspectionFee(inspectionFee);
        dailySettle.setRegistrationFee(registrationFee);
        dailySettle.setDealFee(dealFee);


        Integer normal = invoiceService.getNormalInvoiceNumber(startDate, endDate); //发票数
        Integer anew = invoiceService.getAnewInvoiceNumber(startDate, endDate); //重打发票数

        dailySettle.setNormalInvoiceAmount(normal);
        dailySettle.setAnewInvoiceAmount(anew);

        return dailySettle;
    }

    /**
     * 将日结单存入数据库，并冻结payment
     *
     * @param dailySettle
     */
    @Transactional
    @Override
    public void makeSettleTable(DailySettle dailySettle) {
        //存储日结基本信息
        save(dailySettle);

        //对相应发票和缴费单进行操作
        Integer settleId = dailySettle.getId();

        for (Invoice invoice : invoiceService.findInvoiceAndPaymentByUser(dailySettle.getAdminId(), dailySettle.getStartDate(), dailySettle.getEndDate())) {
            invoice.setDailySettleId(settleId);
            for (Payment payment : invoice.getPaymentList()) {
                payment.setFrozen(true);
                paymentService.update(payment);
            }
            invoiceService.update(invoice);
        }
    }

    /**
     * 确认日结通过
     *
     * @param checkId
     * @param settleId
     * @throws IllegalArgumentException
     */
    @Override
    public void checkSettleTable(Integer checkId, Integer settleId) throws IllegalArgumentException {
        DailySettle dailySettle = findById(settleId);
        if (dailySettle == null)
            throw new IllegalArgumentException("settleId");

        dailySettle.setPass(true);
        dailySettle.setCheckId(checkId);
        update(dailySettle);
    }

    /**
     * 获取上次日结截止时间
     * @param adminId
     * @return
     */
    private Date lastSettleDate(Integer adminId) {
        Date startDate = dailySettleMapper.selectLastTime(adminId);
        if (startDate == null) //第一个日结信息
            startDate = new Date(System.currentTimeMillis() - 10 * 24 * 60 * 60 * 1000);

        return startDate;
    }

    /**
     * 获取日结截止时间
     * @param endDate
     * @return
     * @throws IndexOutOfBoundsException
     */
    private Date endSettleDate(Date endDate) throws IndexOutOfBoundsException{
        if (endDate == null)
            endDate = new Date(System.currentTimeMillis() - 60 * 1000);//获取当前时间前一分钟
        else if (endDate.compareTo(new Date(System.currentTimeMillis())) > 0)
            throw new IndexOutOfBoundsException();

        return endDate;
    }


    @Override
    public ArrayList<DailySettle> getSettleInfo(Integer admin) {
        return dailySettleMapper.selectNotCheckByAdminId(admin);
    }

}
