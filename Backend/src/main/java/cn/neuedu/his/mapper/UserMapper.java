package cn.neuedu.his.mapper;

import cn.neuedu.his.model.User;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

@Component

public interface UserMapper extends MyMapper<User> {
    User getUserByUsername(String username);

    User getUserAllInformationByUsername(String username);
}