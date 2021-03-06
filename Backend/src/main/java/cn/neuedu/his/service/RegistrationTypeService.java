package cn.neuedu.his.service;
import cn.neuedu.his.model.RegistrationType;
import cn.neuedu.his.util.inter.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface RegistrationTypeService extends Service<RegistrationType> {
    RegistrationType getRegistrationTypeByName(String name);
    void deleteRegistrationTypeByName(String name);
    void insertRegisterType(RegistrationType registrationType);
    void deleteRegisterType(Integer id);
    void modifyRegisterType(RegistrationType registrationType);
    RegistrationType selectRegisterType(String name);
}
