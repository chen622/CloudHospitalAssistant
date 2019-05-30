package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DepartmentMapper;
import cn.neuedu.his.model.Department;
import cn.neuedu.his.service.DepartmentService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class DepartmentServiceImpl extends AbstractService<Department> implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    public Department getAllDepartmentInformation(Integer id){
        return departmentMapper.getAllDepartmentInformation(id);
    }

}
