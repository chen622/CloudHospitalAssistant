package cn.neuedu.his.mapper;

import cn.neuedu.his.model.ConstantVariable;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ConstantVariableMapper extends MyMapper<ConstantVariable> {
    List<ConstantVariable> getDepartmentType();
    List<ConstantVariable> getPrimaryConstant();
    ConstantVariable getConstantByName(String name, Integer type);
    ConstantVariable justifyPrimaryType(Integer id);

}