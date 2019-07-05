package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.NonDrugMapper;
import cn.neuedu.his.model.NonDrug;
import cn.neuedu.his.model.PaymentType;
import cn.neuedu.his.service.NonDrugService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.constants.ErrorEnum;
import cn.neuedu.his.util.inter.AbstractService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by ccm on 2019/05/24.
 */
@Service
public class NonDrugServiceImpl extends AbstractService<NonDrug> implements NonDrugService {

    @Autowired
    private NonDrugMapper nonDrugMapper;
    @Autowired
    RedisServiceImpl redisService;

    @Override
    public NonDrug selectNonDrugByName(String name) {
        return nonDrugMapper.selectNonDrugByName(name);
    }

    @Override
    public NonDrug selectNonDrugByCode(String code) {
        return nonDrugMapper.selectNonDrugByCode(code);
    }

    @Override
    public List<NonDrug> findByName(String name) {
        List<NonDrug> list = nonDrugMapper.findByName(name);
        if (list == null)
            list = new ArrayList<>();
        return list;
    }

    @Override
    public JSONObject getAll() {
        List<NonDrug> list = nonDrugMapper.getAll();
        if (list == null)
            list = new ArrayList<>();
        return CommonUtil.successJson(list);
    }

    @Override
    public void insertNonDrug(NonDrug nonDrug) throws Exception {
        Map<String, Integer> payment = redisService.getMapAll("paymentType");
        //检查费药品类型是否存在
        if (!payment.values().contains(nonDrug.getFeeTypeId()))
            throw new RuntimeException("608");

        if (nonDrugMapper.findByNameAccurately(nonDrug.getName()) != null)
            throw new RuntimeException("642");
        //检查执行部门是否存在
        Integer excutiveDepartmentId = nonDrug.getExecutiveDepartment();
        if (excutiveDepartmentId != null) {
            if (this.findById(excutiveDepartmentId) == null) {
                throw new RuntimeException("609");
            }
        }

        this.save(nonDrug);
    }

    @Override
    public NonDrug selectNonDrugUsingName(String name) {
        NonDrug nonDrug = this.selectNonDrugByName(name);

        //检查非药品是否存在
        if (nonDrug == null)
            throw new RuntimeException("608");

        return nonDrug;
    }

    @Override
    public NonDrug selectNonDrugUsingCode(String code) {
        NonDrug nonDrug = this.selectNonDrugByCode(code);

        //检查非药品是否存在
        if (nonDrug == null)
            throw new RuntimeException("608");

        return nonDrug;
    }

    @Override
    public void deleteNonDrug(Integer id) {
        NonDrug nonDrug = this.findById(id);

        if (nonDrug == null)
            throw new RuntimeException("608");

        nonDrug.setDelete(true);
        this.update(nonDrug);
    }

    @Override
    public void modifyNonDrug(NonDrug nonDrug) {

        Integer executiveDepartmentId = nonDrug.getExecutiveDepartment();
        if (executiveDepartmentId != null) {
            if (this.findById(executiveDepartmentId) == null) {
                throw new RuntimeException("609");
            }
        }

        this.update(nonDrug);
    }

    @Override
    public List<PaymentType> getTypeAndNonDrug(String name, String code, Boolean auth) {
        return nonDrugMapper.getTypeAndNonDrug(name, code, auth);
    }

    @Override
    public List<PaymentType> getNonDrugByType(Boolean auth) {
        return nonDrugMapper.getNonDrugByType(auth);
    }

    @Override
    public List<NonDrug> getNonDrugByDepartmentId(Integer id) {
        return nonDrugMapper.getNonDrugByDepartmentId(id);
    }
}
