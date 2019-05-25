package cn.neuedu.his.service.impl;

import cn.neuedu.his.config.Authentication.UserDetails;
import cn.neuedu.his.mapper.UserMapper;
import cn.neuedu.his.model.User;
import cn.neuedu.his.service.UserService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by ccm on 2019/05/24.
 */
@Service
public class UserServiceImpl extends AbstractService<User> implements UserService {

    @Autowired
    private UserMapper userMapper;

}
