package cn.neuedu.his.service;
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

    List<DepartmentKind> getAllDepartmentKindList();

    List<DepartmentKind> getDepartmentKindByClassificationId(Integer id);
}
