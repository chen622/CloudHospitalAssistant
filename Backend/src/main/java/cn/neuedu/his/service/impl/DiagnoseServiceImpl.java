package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DiagnoseMapper;
import cn.neuedu.his.model.Diagnose;
import cn.neuedu.his.service.DiagnoseService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class DiagnoseServiceImpl extends AbstractService<Diagnose> implements DiagnoseService {

    @Autowired
    private DiagnoseMapper diagnoseMapper;

    @Override
    public void deleteByMRT(Integer id) {
        diagnoseMapper.deleteByMRT(id);
    }
}
