package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DepartmentKindMapper;
import cn.neuedu.his.model.DepartmentKind;
import cn.neuedu.his.service.DepartmentKindService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<DepartmentKind> getDepartmentKindList() {
        return departmentKindMapper.getDepartmentKindList();
    }

    /**
     * 获得所有分类及其类型信息
     * @return
     */
    @Override
    public List<DepartmentKind> getAllDepartmentKindList(){
        return this.getDepartmentKindList();
    }

    @Override
    public List<DepartmentKind> getDepartmentKindByClassificationId(Integer id) {
        return departmentKindMapper.getDepartmentKindByClassificationId(id);
    }

}
