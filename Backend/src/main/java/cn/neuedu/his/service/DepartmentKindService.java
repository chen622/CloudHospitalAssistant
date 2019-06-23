package cn.neuedu.his.service;
import cn.neuedu.his.model.Department;
import cn.neuedu.his.model.DepartmentKind;
import cn.neuedu.his.util.inter.Service;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface DepartmentKindService extends Service<DepartmentKind> {
    DepartmentKind getDepartmentKindByName(String kindName);

    List<DepartmentKind> getDepartmentKindList();

    List<DepartmentKind> getDepartmentKindListWithoutDelete();

    List<DepartmentKind> getDepartmentKindByClassificationId(Integer id);


    List<DepartmentKind> getKindAndDepartment();

    List<DepartmentKind> getKindAndDepartmentWithType(Integer type);//获得小类及其下属信息

}
