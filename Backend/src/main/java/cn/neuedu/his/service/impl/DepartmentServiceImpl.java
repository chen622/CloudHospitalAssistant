package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DepartmentMapper;
import cn.neuedu.his.model.*;
import cn.neuedu.his.service.*;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.constants.Constants;
import cn.neuedu.his.util.constants.ErrorEnum;
import cn.neuedu.his.util.inter.AbstractService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ccm on 2019/05/24.
 */
@Service
public class DepartmentServiceImpl extends AbstractService<Department> implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private PaymentTypeService paymentTypeService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    RedisServiceImpl redisService;

    @Override
    public List<Department> getAllDepartmentInformation() {
        return departmentMapper.getAllDepartmentInformation();
    }

    @Override
    public Department getDepartmentByName(String name) {
        return departmentMapper.getDepartmentByName(name);
    }

    @Override
    public List<Department> getAllDepartmentInformationByClassificationId(Integer classificationId) {
        return departmentMapper.getAllDepartmentInformationByClassificationId(classificationId);
    }

    @Override
    public List<Department> getDepartmentInformation() {
        List<Department> departments = this.getAllDepartmentInformation();

        if (departments == null)
            departments = new ArrayList<>();
        return departments;
    }

    @Override
    public void deleteDepartmentInformation(Integer id) {

        Department department = this.findById(id);

        //检测部门是否存在
        if (department == null)
            throw new RuntimeException("610");

        department.setDelete(true);

        this.update(department);

    }

    @Override
    public void addDepartment(Department department) {

        //检测部门是否存在
        if (this.getDepartmentByName(department.getName()) != null)
            throw new RuntimeException("611");
        //return CommonUtil.errorJson(ErrorEnum.E_611);

        //检测部门类型是否存在
        if (this.findById(department.getKindId()) == null)
            throw new RuntimeException("612");
        //return CommonUtil.errorJson(ErrorEnum.E_612);

        this.save(department);

    }

    @Override
    public void modifyDepartment(Department department) throws Exception {
        //检测部门是否存在
        if (this.getDepartmentByName(department.getName()) != null)
            throw new RuntimeException("611");


        try {
            Map<String ,Integer> map=redisService.getMapAll("departmentType");
            //检测部门类型是否存在
            if (!map.containsValue(department.getKindId()))
                throw new RuntimeException("612");
        } catch (Exception e) {
            throw new Exception("802");
        }
        this.update(department);
    }

    @Override
    public List<Department> getDepartmentListByName(String name) {
        return departmentMapper.getDepartmentListByName(name);
    }

    /**
     * 门诊科室工作量统计
     *
     * @param classification：临床还是医技
     * @param startDate
     * @param endDate
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public JSONArray workCalculate(Integer classification, Date startDate, Date endDate) throws IllegalArgumentException {
        JSONArray result = new JSONArray();
        ArrayList<Department> departmentList = departmentMapper.getDepartmentByClassification(classification);
        if (departmentList.isEmpty())
            throw new IllegalArgumentException("classification");

        //取出所有paymentType的id和名字
        Map<Integer, String> paymentTypeMap = new HashMap<>();
        for (PaymentType paymentType : paymentTypeService.findAllNotDelete()) {
            if (paymentType.getId() > 100) {
                paymentTypeMap.put(paymentType.getId(), paymentType.getName());
            }
        }

        for (Department department : departmentList) {
            //使用map记录每个科室的每个paymentType对应的总额
            Map<Integer, BigDecimal> feeMap = new HashMap<>();
            for (Integer id : paymentTypeMap.keySet()) {
                feeMap.put(id, new BigDecimal(0));
            }
            Integer invoiceNumber = 0; //每个科室发票总数
            Integer doctorVisitNumber = 0; //每个科室医生看诊人数总数
            BigDecimal totalFee = new BigDecimal(0); //科室总计金额
            for (User user : userService.findUserByDepartmentId(department.getId())) { //获取科室中的医生
                for (Payment payment : paymentService.findByAllDoctor(user.getId(), startDate, endDate)) {
                    //更新某缴费项目类型的金额数据
                    feeMap.put(payment.getPaymentTypeId(), feeMap.get(payment.getPaymentTypeId()).add(payment.getUnitPrice().multiply(new BigDecimal(payment.getQuantity()))));
                }

                //计算发票总数
                invoiceNumber = invoiceNumber + invoiceService.getInvoiceNumberByAllDoctor(user.getId(), startDate, endDate);

                //计算医生看诊人数
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                doctorVisitNumber = doctorVisitNumber + doctorService.registrationNum(user.getId(), format.format(startDate), format.format(endDate));
            }

            //将获得数据记录进JSONObject
            JSONObject detail = new JSONObject();
            detail.put("department", department);
            detail.put("发票数", invoiceNumber);
            detail.put("看诊人数", doctorVisitNumber);
            for (Integer key : feeMap.keySet()) {
                totalFee.add(feeMap.get(key));
                detail.put(paymentTypeMap.get(key), feeMap.get(key));
            }
            detail.put("合计", totalFee);

            result.add(detail);
        }

        return result;
    }
}
