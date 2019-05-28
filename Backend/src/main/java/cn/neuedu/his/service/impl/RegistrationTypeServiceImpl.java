package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.RegistrationTypeMapper;
import cn.neuedu.his.model.RegistrationType;
import cn.neuedu.his.service.RegistrationTypeService;
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
    public RegistrationType getRegistrationTypeByName(String name){
        return registrationTypeMapper.getRegistrationTypeByName(name);
    }
    public void deleteRegistrationTypeByName(String name){
        registrationTypeMapper.deleteRegistrationTypeByName(name);
    }
}
