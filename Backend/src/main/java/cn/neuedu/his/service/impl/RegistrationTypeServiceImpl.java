package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.RegistrationTypeMapper;
import cn.neuedu.his.model.RegistrationType;
import cn.neuedu.his.service.RegistrationTypeService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.constants.ErrorEnum;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class RegistrationTypeServiceImpl extends AbstractService<RegistrationType> implements RegistrationTypeService {

    @Autowired
    RegistrationTypeMapper registrationTypeMapper;

    @Override
    public RegistrationType getRegistrationTypeByName(String name){
        return registrationTypeMapper.getRegistrationTypeByName(name);
    }

    @Override
    public void deleteRegistrationTypeByName(String name){
        registrationTypeMapper.deleteRegistrationTypeByName(name);
    }

    @Override
    public void insertRegisterType(RegistrationType registrationType) {
        //判断挂号类型是否已经存在
        if (this.getRegistrationTypeByName(registrationType.getName())!= null)
            throw new RuntimeException("603");

        this.save(registrationType);
    }

    @Override
    public void deleteRegisterType(Integer id) {
        //判断挂号类型是否存在
        if (this.findById(id) == null)
            throw new RuntimeException("604");

        this.deleteById(id);
    }

    @Override
    public void modifyRegisterType(RegistrationType registrationType) {
        //判断挂号名称是否存在
        if (this.getRegistrationTypeByName(registrationType.getName()) == null)
            throw new RuntimeException("604");

        this.update(registrationType);
    }

    @Override
    public RegistrationType selectRegisterType(String name) {
        RegistrationType registrationType = this.getRegistrationTypeByName(name);
        //判断挂号类型是否存在
        if (registrationType== null)
            throw new RuntimeException("604");

        return registrationType;
    }


}
