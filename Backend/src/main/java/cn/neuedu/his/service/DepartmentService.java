package cn.neuedu.his.service;
import cn.neuedu.his.model.Department;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.constants.ErrorEnum;
import cn.neuedu.his.util.inter.Service;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface DepartmentService extends Service<Department> {

    List<Department> getAllDepartmentInformation();

    Department getDepartmentByName(String name);

    List<Department> getAllDepartmentInformationByClassificationId(Integer classificationId);

    List<Department> getDepartmentInformation();

    void deleteDepartmentInformation(Integer id);

    void addDepartment(Department department);

    void modifyDepartment(Department department);


}
