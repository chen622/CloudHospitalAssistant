package cn.neuedu.his.service;
import cn.neuedu.his.model.Doctor;
import cn.neuedu.his.model.User;
import cn.neuedu.his.util.inter.Service;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface UserService extends Service<User> {
    User getUserByUsername(String username);

    //插入用户
    void insertUser(User user, Doctor doctor) throws Exception;

    //更新用户
    void updateUser(User user, Doctor doctor) throws Exception;

    User getUserAllInformationByName(String username);

    List<User> findAllUsers();

    ArrayList<User> findUserByDepartmentId(Integer departmentId);

    ArrayList<User> findAllWithName();

    List<User> findUser(String name);

    void deleteUser(Integer id) throws Exception;

    ArrayList<User> findAllTollKeeper() throws Exception;

    List<User> getUserWithDocByDept(Integer typeId,Integer deptId);
}
