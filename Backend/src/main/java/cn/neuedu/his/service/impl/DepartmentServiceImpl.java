package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DepartmentMapper;
import cn.neuedu.his.model.Department;
import cn.neuedu.his.service.DepartmentService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.constants.ErrorEnum;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class DepartmentServiceImpl extends AbstractService<Department> implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> getAllDepartmentInformation(){
        return departmentMapper.getAllDepartmentInformation();
    }

    @Override
    public Department getDepartmentByName(String name) {
        return departmentMapper.getDepartmentByName(name);
    }

    @Override
    public List<Department> getAllDepartmentInformationByClassificationId(Integer classificationId) {
        return departmentMapper.getAllDepartmentInformationByClassificationId(classificationId);
    }

    @Override
    public List<Department> getDepartmentInformation() {
            List<Department> departments = this.getAllDepartmentInformation();

            if (departments == null)
                throw new RuntimeException("610");

            return departments;
    }


}
