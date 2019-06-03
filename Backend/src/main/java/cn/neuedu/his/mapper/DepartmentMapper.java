package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Department;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DepartmentMapper extends MyMapper<Department> {
    Department getAllDepartmentInformation(Integer id);
    Department getDepartmentByName(String name);

    List<Department> getAllDepartmentInformationByClassificationId(Integer classificationId);
}