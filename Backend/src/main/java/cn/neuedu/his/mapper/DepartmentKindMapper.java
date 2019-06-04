package cn.neuedu.his.mapper;

import cn.neuedu.his.model.DepartmentKind;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DepartmentKindMapper extends MyMapper<DepartmentKind> {

    DepartmentKind getDepartmentKindByName(String kindName);

    List<DepartmentKind> getDepartmentKindByType(Integer type);

}