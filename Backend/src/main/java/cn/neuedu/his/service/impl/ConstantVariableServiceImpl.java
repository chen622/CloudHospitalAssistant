package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.ConstantVariableMapper;
import cn.neuedu.his.model.ConstantVariable;
import cn.neuedu.his.service.ConstantVariableService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class ConstantVariableServiceImpl extends AbstractService<ConstantVariable> implements ConstantVariableService {

    @Autowired
    private ConstantVariableMapper constantVariableMapper;


    @Override
    public List<ConstantVariable> getDepartmentType() {
        return constantVariableMapper.getDepartmentType();
    }

    @Override
    public List<ConstantVariable> getPrimaryConstant() {
        return constantVariableMapper.getPrimaryConstant();
    }

    @Override
    public ConstantVariable getConstantByName(Integer type, String name) {
        return constantVariableMapper.getConstantByName(name,type);
    }

    public ConstantVariable justifyPrimaryType(Integer id){
        return constantVariableMapper.justifyPrimaryType(id);
    }

    @Override
    public void insertConstant(ConstantVariable constantVariable) {
        //判断大类type0是否存在
        if (constantVariable.getType() != 0){
            ConstantVariable constantVariable1 = this.justifyPrimaryType(constantVariable.getType());
            if (constantVariable1 == null)
                throw new RuntimeException("629");
        }

        //判断常量是否存在
        if (this.getConstantByName(constantVariable.getType(),constantVariable.getName()) != null)
            throw new RuntimeException("630");

        this.save(constantVariable);
    }

    @Override
    public void deleteConstant(Integer id){

        if (this.findById(id) == null)
            throw new RuntimeException("629");

        this.deleteById(id);
    }

    @Override
    public void modifyConstant(ConstantVariable constantVariable) {
        //判断大类type0是否存在
        if (constantVariable.getType() != 0){
            ConstantVariable constantVariable1 = this.justifyPrimaryType(constantVariable.getType());
            if (constantVariable1 == null)
                throw new RuntimeException("629");
        }

        //判断常量是否存在
        if (this.getConstantByName(constantVariable.getType(),constantVariable.getName()) != null)
            throw new RuntimeException("630");

        this.update(constantVariable);
    }
}
