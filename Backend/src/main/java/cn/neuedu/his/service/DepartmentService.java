package cn.neuedu.his.service;
import cn.neuedu.his.model.Department;
import cn.neuedu.his.util.inter.Service;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface DepartmentService extends Service<Department> {

    Department getAllDepartmentInformation(Integer id);

    Department getDepartmentByName(String name);

    List<Department> getAllDepartmentInformationByClassificationId(Integer classificationId);

}
