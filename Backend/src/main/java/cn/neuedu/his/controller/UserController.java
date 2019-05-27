package cn.neuedu.his.controller;

import cn.neuedu.his.model.Department;
import cn.neuedu.his.model.Doctor;
import cn.neuedu.his.model.User;
import cn.neuedu.his.service.DepartmentService;
import cn.neuedu.his.service.DoctorService;
import cn.neuedu.his.service.UserService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Arrays;

import static cn.neuedu.his.util.constants.Constants.doctorTypeList;
import static cn.neuedu.his.util.constants.Constants.userTypeList;

/**
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/login/{id}")
    public JSONObject login(@RequestBody JSONObject jsonObject, Authentication authentication) {
        return CommonUtil.successJson(authentication.getName());
    }


    @PostMapping("/register")
    public JSONObject register(@RequestBody JSONObject jsonObject) {

        User user = JSONObject.toJavaObject(jsonObject,User.class);

        if ( userService.getUserByUsername(user.getUsername())!= null){
            return CommonUtil.errorJson(ErrorEnum.E_600);
        }
        //判断身份证信息长度

        if (user.getIdentifyId().length() != 18){
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("身份信息"));}

        //创建用户对象
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //判断部门是否存在
        Department department = departmentService.findById(user.getDepartmentId());
        if (department == null) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("部门"));
        }

        Integer typeId = user.getTypeId();
        //判断输入type_id是否正确
        if (!userTypeList.contains(typeId))
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("用户类别"));

        //储存user数据
        userService.save(user);

        //类别属于医生
        if (doctorTypeList.contains(typeId)) {
            //取得当前user的id
            Integer id = userService.getUserByUsername(user.getUsername()).getId();

            //创立医生对象
            Doctor doctor = JSONObject.toJavaObject(jsonObject,Doctor.class);
            doctor.setId(id);

            //储存doctor数据
            doctorService.save(doctor);
        }
        return CommonUtil.successJson(user);

    }

}
