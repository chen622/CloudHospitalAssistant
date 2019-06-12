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
    @Autowired
    private RedisServiceImpl redisService;


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

    /**
     *
     * @param constantVariable
     */
    @Override
    public void insertConstant(ConstantVariable constantVariable,String key) {
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
        if(constantVariable.getType() != 0){
            redisService.setHash(key,constantVariable.getName(),constantVariable.getId().toString());
        }
    }

    @Override
    public void deleteConstant(Integer id,String type){

        ConstantVariable constantVariable = this.findById(id);
        if (constantVariable == null)
            throw new RuntimeException("629");

        if (constantVariable.getType() !=0)
            redisService.deleteHash(type, constantVariable.getName(),constantVariable.getId().toString());
        else if (this.getConstantByType(constantVariable.getId()) != null){
            throw new RuntimeException("633");
        }
        constantVariable.setDelete(true);
        this.update(constantVariable);
        redisService.deleteHash(type,constantVariable.getName(),constantVariable.getId().toString());
    }

    @Override
    public void modifyConstant(ConstantVariable constantVariable, String type) {
        //判断大类type0是否存在
        if (constantVariable.getType() != 0){
            ConstantVariable constantVariable1 = this.justifyPrimaryType(constantVariable.getType());
            if (constantVariable1 == null)
                throw new RuntimeException("629");
        }else if (this.getConstantByType(constantVariable.getId()) != null){
            throw new RuntimeException("633");
        }

        //判断常量是否存在
        ConstantVariable constantVariable1 = this.getConstantByName(constantVariable.getType(),constantVariable.getName());
        if (constantVariable1 != null && constantVariable1.getId()!= constantVariable.getId())
            throw new RuntimeException("630");

        constantVariable.setDelete(false);
        this.update(constantVariable);
        redisService.setHash(type,constantVariable.getName(),constantVariable.getId().toString());
    }

    @Override
    public List<ConstantVariable> getConstantByType(Integer type) {
        return constantVariableMapper.getConstantByType(type);
    }
}
