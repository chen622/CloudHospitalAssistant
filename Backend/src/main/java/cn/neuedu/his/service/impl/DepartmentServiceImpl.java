package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DepartmentMapper;
import cn.neuedu.his.model.Department;
import cn.neuedu.his.model.Doctor;
import cn.neuedu.his.model.User;
import cn.neuedu.his.service.DepartmentService;
import cn.neuedu.his.service.DoctorService;
import cn.neuedu.his.service.UserService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.constants.ErrorEnum;
import cn.neuedu.his.util.inter.AbstractService;
import com.alibaba.fastjson.JSON;
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
public class DepartmentServiceImpl extends AbstractService<Department> implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private UserService userService;

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

    @Override
    public void deleteDepartmentInformation(Integer id){

        Department department = this.findById(id);

        //检测部门是否存在
        if (department == null)
            throw new RuntimeException("610");

        department.setDelete(true);

        this.update(department);

    }

    @Override
    public void addDepartment(Department department) {

        //检测部门是否存在
        if (this.getDepartmentByName(department.getName()) != null)
            throw new RuntimeException("611");
            //return CommonUtil.errorJson(ErrorEnum.E_611);

        //检测部门类型是否存在
        if (this.findById(department.getKindId()) == null)
            throw new RuntimeException("612");
            //return CommonUtil.errorJson(ErrorEnum.E_612);

        this.save(department);

    }

    @Override
    public void modifyDepartment(Department department) {
        //检测部门是否存在
        if (this.getDepartmentByName(department.getName()) != null)
            throw new RuntimeException("611");

        //检测部门类型是否存在
        if (this.findById(department.getKindId()) == null)
            throw  new RuntimeException("612");

        this.update(department);
    }

    @Override
    public List<Department> getDepartmentListByName(String name) {
        return departmentMapper.getDepartmentListByName(name);
    }


}
