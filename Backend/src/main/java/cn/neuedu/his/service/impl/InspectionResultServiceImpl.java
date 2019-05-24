package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.InspectionResultMapper;
import cn.neuedu.his.model.InspectionResult;
import cn.neuedu.his.service.InspectionResultService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class InspectionResultServiceImpl extends AbstractService<InspectionResult> implements InspectionResultService {

    @Autowired
    private InspectionResultMapper inspectionResultMapper;

}
