package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Department;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public interface DepartmentMapper extends MyMapper<Department> {

    List<Department> getByKindId(Integer kindId);

    List<Department> getAllDepartmentInformation();//获得所有部门的类别，分类及详细信息

    Department getDepartmentByName(String name);//根据准确名字获得部门

    List<Department> getAllDepartmentInformationByClassificationId(Integer classificationId);

    ArrayList<Department> getDepartmentByClassification(Integer classification);

    List<Department> getDepartmentListByName(String name);

}