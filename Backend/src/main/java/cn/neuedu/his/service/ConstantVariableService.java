package cn.neuedu.his.service;
import cn.neuedu.his.model.ConstantVariable;
import cn.neuedu.his.util.inter.Service;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface ConstantVariableService extends Service<ConstantVariable> {
    List<ConstantVariable> getDepartmentType(Integer type);

}
