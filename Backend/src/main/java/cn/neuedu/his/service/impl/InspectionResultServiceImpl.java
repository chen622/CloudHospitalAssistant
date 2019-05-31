package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.InspectionResultMapper;
import cn.neuedu.his.model.InspectionResult;
import cn.neuedu.his.service.InspectionResultService;
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
public class InspectionResultServiceImpl extends AbstractService<InspectionResult> implements InspectionResultService {

    @Autowired
    private InspectionResultMapper inspectionResultMapper;

    @Override
    public JSONObject getInspectionResult(Integer id) {
        List<InspectionResult> list=inspectionResultMapper.getInspectionResult(id);
        if(list==null)
            list= new ArrayList<>();
        return CommonUtil.successJson(list);
    }
}
