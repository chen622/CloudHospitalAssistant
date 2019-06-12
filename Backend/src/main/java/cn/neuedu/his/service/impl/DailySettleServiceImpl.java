package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DailySettleMapper;
import cn.neuedu.his.model.DailySettle;
import cn.neuedu.his.model.Invoice;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.model.User;
import cn.neuedu.his.service.*;
import cn.neuedu.his.util.constants.Constants;
import cn.neuedu.his.util.inter.AbstractService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import static cn.neuedu.his.util.constants.Constants.*;

/**
 *
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
     * 生成日结表
     * @param makeId
     * @param adminId
     * @param endDate
     * @throws IllegalArgumentException
     */
    @Transactional
    @Override
    public void makeSettleTable(Integer makeId, Integer adminId, Date endDate) throws IllegalArgumentException{
        //确定起止时间
        if (endDate == null)
            endDate = new Date(System.currentTimeMillis() - 60 * 1000);//获取当前时间前一分钟
        DailySettle lastDailySettle = dailySettleMapper.selectLastTime(adminId);
        Date startDate;
        if (lastDailySettle == null)
            startDate = new Date(System.currentTimeMillis() - 10 * 24 * 60 * 60 * 1000);
        else
            startDate = lastDailySettle.getStartDate();

        //获取信息
        User user = userService.findUserAndInvoiceAndPaymentDuringDate(adminId, startDate, endDate);

        if (user == null)
            throw new IllegalArgumentException("userId");

        //存储日结基本信息
        DailySettle dailySettle = new DailySettle();
        dailySettle.setAdminId(adminId);
        dailySettle.setMakeId(makeId);
        dailySettle.setMakeDate(new Date(System.currentTimeMillis()));
        dailySettle.setStartDate(startDate);
        dailySettle.setEndDate(endDate);
        save(dailySettle);

        //对相应发票和缴费单进行操作
        Integer settleId = dailySettle.getId();
        Integer normal = 0; //发票数
        Integer anew = 0; //重打发票数
        //各类费用
        BigDecimal drugFee = new BigDecimal(0);
        BigDecimal inspectionFee = new BigDecimal(0);
        BigDecimal registrationFee = new BigDecimal(0);
        //对每一张发票进行日结
        for (Invoice invoice: user.getInvoiceList()) {
            //确定该发票的金额为什么类型（挂号、检查、药物）
            if (invoice.getPaymentList().isEmpty())
                throw new IllegalArgumentException("invoiceId");
            Integer totalType = paymentTypeService.getTotalPaymentType(invoice.getPaymentList().get(0).getPaymentTypeId());
            if (totalType.equals(Constants.REGISTRATION_PAYMENT_TYPE))
                registrationFee = registrationFee.add(invoice.getPriceAmount());
            else if (totalType.equals(Constants.NON_DRUG_PAYMENT_TYPE))
                inspectionFee = inspectionFee.add(invoice.getPriceAmount());
            else if (totalType.equals(Constants.DRUG_PAYMENT_TYPE ))
                drugFee = drugFee.add(invoice.getPriceAmount());

            //冻结所有缴费单
            for (Payment payment: invoice.getPaymentList()) {
                payment.setFrozen(true);
                paymentService.update(payment);
            }

            //更新发票信息（日结id）
            invoice.setDailySettleId(settleId);
            invoiceService.update(invoice);

            //计算总发票数
            normal++;
            anew = anew + invoice.getAnewAmount();
        }

        //更新日结表
        dailySettle.setDrugFee(drugFee);
        dailySettle.setInspectionFee(inspectionFee);
        dailySettle.setRegistrationFee(registrationFee);
        dailySettle.setNormalInvoiceAmount(normal);
        dailySettle.setAnewInvoiceAmount(anew);
        dailySettle.setTotalFee(drugFee.add(inspectionFee).add(registrationFee));
        update(dailySettle);
    }

    /**
     * 确认日结通过
     * @param checkId
     * @param settleId
     * @throws IllegalArgumentException
     */
    @Override
    public void checkSettleTable(Integer checkId, Integer settleId) throws IllegalArgumentException{
        DailySettle dailySettle = findById(settleId);
        if (dailySettle == null)
            throw new IllegalArgumentException("settleId");

        dailySettle.setPass(true);
        dailySettle.setCheckId(checkId);
        update(dailySettle);
    }



    @Override
    public ArrayList<DailySettle> getSettleInfo(Integer admin) {
        return dailySettleMapper.selectNotCheckByAdminId(admin);
    }

}
