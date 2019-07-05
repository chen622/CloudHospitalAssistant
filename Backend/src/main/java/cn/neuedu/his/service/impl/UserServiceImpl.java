package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.UserMapper;
import cn.neuedu.his.model.Department;
import cn.neuedu.his.model.Doctor;
import cn.neuedu.his.model.User;
import cn.neuedu.his.service.DepartmentService;
import cn.neuedu.his.service.DoctorService;
import cn.neuedu.his.service.UserService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by ccm on 2019/05/24.
 */
@Service
public class UserServiceImpl extends AbstractService<User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    RedisServiceImpl redisService;

    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Transactional
    public void insertUser(User user, Doctor doctor) throws Exception {//判断身份证信息长度
        if (user.getIdentifyId().length() != 18) {
            throw new RuntimeException("501.1");
            //return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("身份信息"));
        }

        //创建用户对象
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User user1 = this.getUserByUsername(user.getUsername());
        if (user1 != null && !user1.getId().equals(user.getId()))
            throw new RuntimeException("600");
        //return CommonUtil.errorJson(ErrorEnum.E_600);

        //判断部门是否存在
        Department department = departmentService.findById(user.getDepartmentId());
        if (department == null) {
            throw new RuntimeException("501.2");
            //return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("部门"));
        }

        Integer typeId = user.getTypeId();

        Map<String, Integer> map = redisService.getMapAll("userType");

        //判断输入type_id是否正确
        if (!map.values().contains(typeId))
            throw new RuntimeException("501.3");
        //return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("用户类别"));

        //储存user数据
        this.save(user);

        Map<String, Integer> map2 = redisService.getMapAll("doctor");

        //类别属于医生
        if (map2.values().contains(typeId)) {
            //取得当前user的id
            Integer id = userMapper.getUserByUsername(user.getUsername()).getId();

            //判断医生职称类型是否正确
            Map<String, Integer> title = redisService.getMapAll("title");
            if (!title.values().contains(doctor.getTitleId())) {
                throw new RuntimeException("501.4");
            }

            doctor.setId(id);
            //储存doctor数据
            doctorService.save(doctor);
        }
    }

    /**
     * 修改数据
     *
     * @param user
     * @param doctor
     * @throws RuntimeException
     */
    @Override
    @Transactional
    public void updateUser(User user, Doctor doctor) throws Exception {
        //判断用户名是否重复
        User user1 = this.getUserByUsername(user.getUsername());
        if (user1 != null && !user1.getId().equals(user.getId()))
            throw new RuntimeException("600");
        //return CommonUtil.errorJson(ErrorEnum.E_600);

        Map<String, Integer> map = null;
        try {
            map = redisService.getMapAll("userType");
        } catch (Exception e) {
            throw new RuntimeException("802");
            //CommonUtil.errorJson(ErrorEnum.E_802);
        }
        if (map == null)
            throw new RuntimeException("802");
        //return   CommonUtil.errorJson(ErrorEnum.E_802);
        //判断type_id是否正确
        if (!map.values().contains(user.getTypeId()))
            throw new RuntimeException("501.1");
        //return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("用户类型"));

        //判断user的身份证号是否正确
        if (user.getIdentifyId().length() != 18)
            throw new RuntimeException("501.2");
        //return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("身份信息"));

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        this.update(user);

        user = userMapper.getUserByUsername(user.getUsername());

        Map<String, Integer> map2 = redisService.getMapAll("doctor");

        //修改医生信息
        if (map2.values().contains(user.getTypeId())) {
            Map<String, Integer> title = redisService.getMapAll("title");
            //判断医生职称是否正确
            if (!title.values().contains(doctor.getTitleId())) {
                throw new RuntimeException("501.3");
                //return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("医生职称"));
            }
            doctor.setId(user.getId());
            doctorService.update(doctor);
        } else {
            Doctor doctor1 = doctorService.findById(user.getId());
            if (doctor1 != null)
                doctor1.setDelete(true);
            doctorService.update(doctor1);
        }
    }

    @Override
    public void deleteUser(Integer id) throws Exception {

        //获取user
        User user = this.findById(id);

        //判断被删除用户是否存在
        if (user == null)
            throw new RuntimeException("601");

        Map<String, Integer> map = redisService.getMapAll("doctor");

        //判断是否要先将doctor表中的数据删除
        if (map.values().contains(user.getTypeId()) == true) {
            Doctor doctor = doctorService.findById(id);
            doctor.setDelete(true);
            doctorService.update(doctor);
        }

        //删除用户表中的信息
        user.setDelete(true);
        this.update(user);
    }

    /**
     * 获取所有收费员
     *
     * @return
     */
    @Override
    public ArrayList<User> findAllTollKeeper() throws Exception {
        Map<String, Integer> map = redisService.getMapAll("userType");
        return userMapper.getAllTollKeeper(map.get("挂号收费员"));
    }

    @Override
    public User getUserAllInformationByName(String username) {
        return userMapper.getUserAllInformationByUsername(username);
    }

    @Override
    public List<User> findAllUsers() {
        return this.findAll();
    }

    @Override
    public ArrayList<User> findUserByDepartmentId(Integer departmentId) {
        return userMapper.getUserByDepartmentId(departmentId);
    }

    @Override
    public ArrayList<User> findAllWithName() {
        return userMapper.findAllWithName();
    }

    @Override
    public List<User> findUser(String name) {
        return userMapper.findUser(name);
    }

    @Override
    public List<User> getUserWithDocByDept(Integer typeId, Integer deptId) {
        List<User> users = userMapper.getUserWithDocByDept(typeId, deptId);
        if (users == null) {
            users = new ArrayList<>();
        }
        return users;
    }
}
