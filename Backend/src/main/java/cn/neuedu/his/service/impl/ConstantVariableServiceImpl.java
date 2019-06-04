package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.ConstantVariableMapper;
import cn.neuedu.his.model.ConstantVariable;
import cn.neuedu.his.service.ConstantVariableService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class ConstantVariableServiceImpl extends AbstractService<ConstantVariable> implements ConstantVariableService {

    @Autowired
    private ConstantVariableMapper constantVariableMapper;


    @Override
    public List<ConstantVariable> getDepartmentType() {
        return constantVariableMapper.getDepartmentType();
    }
}
