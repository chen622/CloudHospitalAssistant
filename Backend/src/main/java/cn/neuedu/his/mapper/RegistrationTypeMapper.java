package cn.neuedu.his.mapper;

import cn.neuedu.his.model.RegistrationType;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

@Component

public interface RegistrationTypeMapper extends MyMapper<RegistrationType> {
    RegistrationType getRegistrationTypeByName(String name);

    void deleteRegistrationTypeByName(String name);
}