package cn.neuedu.his.config.Authentication;

import cn.neuedu.his.mapper.UserMapper;
import cn.neuedu.his.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.getUserByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("");
        else
            return new cn.neuedu.his.config.Authentication.UserDetails(user.getId(), user.getUsername(), user.getPassword(),user.getTypeId());
    }


}
