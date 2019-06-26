package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DrugTemplateMapper;
import cn.neuedu.his.model.DrugTemplate;
import cn.neuedu.his.model.Prescription;
import cn.neuedu.his.service.DrugTemplateService;
import cn.neuedu.his.service.PrescriptionService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.constants.ErrorEnum;
import cn.neuedu.his.util.inter.AbstractService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class DrugTemplateServiceImpl extends AbstractService<DrugTemplate> implements DrugTemplateService {

    @Autowired
    private DrugTemplateMapper drugTemplateMapper;
    @Autowired
    private PrescriptionService prescriptionService;


    @Override
    public List<DrugTemplate> getPrescriptionsTemByName(String name) {
        return drugTemplateMapper.getPrescriptionsTemByName(name);
    }

    @Override
    @Transactional
    public JSONObject deleteTem(Integer id) {
        DrugTemplate template=findById(id);
        if(template==null){
            return CommonUtil.errorJson(ErrorEnum.E_901);
        }
        deleteById(id);
        prescriptionService.deleteByTemplateId(id);
        return CommonUtil.successJson();
    }
}
