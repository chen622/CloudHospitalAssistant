package cn.neuedu.his.service;
import cn.neuedu.his.model.User;
import cn.neuedu.his.util.inter.Service;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface UserService extends Service<User> {
    User getUserByUsername(String username);

    void insertUserOperation(User user);

    User getUserAllInformationByName(String username);

    List<User> findAllUsers();



}
