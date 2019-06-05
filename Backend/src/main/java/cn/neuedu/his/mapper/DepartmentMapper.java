package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Department;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public interface DepartmentMapper extends MyMapper<Department> {

    List<Department> getAllDepartmentInformation();

    Department getDepartmentByName(String name);

    List<Department> getAllDepartmentInformationByClassificationId(Integer classificationId);

    ArrayList<Department> getDepartmentByClassification(Integer classification);

}