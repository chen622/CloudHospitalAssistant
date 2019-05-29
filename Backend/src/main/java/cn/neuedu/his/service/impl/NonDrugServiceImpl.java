package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.NonDrugMapper;
import cn.neuedu.his.model.NonDrug;
import cn.neuedu.his.service.NonDrugService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.inter.AbstractService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class NonDrugServiceImpl extends AbstractService<NonDrug> implements NonDrugService {

    @Autowired
    private NonDrugMapper nonDrugMapper;

    @Override
    public NonDrug selectNonDrugByName(String name) {
        return nonDrugMapper.selectNonDrugByName(name);
    }

    @Override
    public NonDrug selectNonDrugByCode(String code) {
        return nonDrugMapper.selectNonDrugByCode(code);
    }
    @Override
    public JSONObject findByName(String name) {
        List<NonDrug> list=nonDrugMapper.findByName(name);
        if(list!=null)
            list=new ArrayList<>();
        return CommonUtil.successJson(list);
    }

    @Override
    public JSONObject getAll() {
        List<NonDrug> list=nonDrugMapper.getAll();
        if(list!=null)
            list=new ArrayList<>();
        return CommonUtil.successJson(list);
    }
}
