package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DepartmentKindMapper;
import cn.neuedu.his.model.DepartmentKind;
import cn.neuedu.his.service.DepartmentKindService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class DepartmentKindServiceImpl extends AbstractService<DepartmentKind> implements DepartmentKindService {

    @Autowired
    private DepartmentKindMapper departmentKindMapper;

    @Override
    public DepartmentKind getDepartmentKindByName (String kindName) {
        return departmentKindMapper.getDepartmentKindByName(kindName);
    }
}
