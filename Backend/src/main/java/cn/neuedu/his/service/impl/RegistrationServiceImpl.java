package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.RegistrationMapper;
import cn.neuedu.his.model.Registration;
import cn.neuedu.his.service.RegistrationService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class RegistrationServiceImpl extends AbstractService<Registration> implements RegistrationService {


    @Autowired
    private RegistrationMapper registrationMapper;

    public List<Registration> getAllWaitingRegistration(Integer doctorID){
        return registrationMapper.getAllWaitingRegistration(doctorID);
    }

    public  List<Registration> getRegistrationByPatientName(String name,Integer doctorID){
        return registrationMapper.getRegistrationByPatientName(name,doctorID);
    }
}
