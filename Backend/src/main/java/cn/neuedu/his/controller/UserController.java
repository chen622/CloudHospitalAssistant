package cn.neuedu.his.controller;

import cn.neuedu.his.model.Department;
import cn.neuedu.his.model.Doctor;
import cn.neuedu.his.model.User;
import cn.neuedu.his.service.DepartmentService;
import cn.neuedu.his.service.DoctorService;
import cn.neuedu.his.service.UserService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static cn.neuedu.his.util.constants.Constants.*;

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

    /**
     * 注册用户信息
     * @param jsonObject 用户信息
     * @return 是否成功
     */
    @PostMapping("/register")
    public JSONObject register(@RequestBody JSONObject jsonObject) {

        User user = JSONObject.toJavaObject(jsonObject,User.class);

        //判断身份证信息长度
        if (user.getIdentifyId().length() != 18){
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("身份信息"));}

        //创建用户对象
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //
        if(userService.getUserByUsername(user.getUsername()) != null)
            return CommonUtil.errorJson(ErrorEnum.E_600);

        //判断部门是否存在
        Department department = departmentService.findById(user.getDepartmentId());
        if (department == null) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("部门"));
        }

        Integer typeId = user.getTypeId();
        //判断输入type_id是否正确
        if (!USER_TYPE_LIST.contains(typeId))
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("用户类别"));

        //类别属于医生
        if (DOCTOR_TYPE_LIST.contains(typeId)) {
            //取得当前user的id
            Integer id = userService.getUserByUsername(user.getUsername()).getId();

            //创立医生对象
            Doctor doctor = JSONObject.toJavaObject(jsonObject,Doctor.class);
            //判断医生职称类型是否正确

            if (!DOCTOR_TITLE_TYPE_LIST.contains(doctor.getTitleId())) {
                return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("医生职称"));
            }

            doctor.setId(id);
            //储存doctor数据
            doctorService.save(doctor);
        }

        //储存user数据
        userService.save(user);

        return CommonUtil.successJson(user);
    }

    /**
     * 管理员删除信息
     * @param username
     * @param authentication
     * @return
     */
    @PostMapping("/delete/{username}")
    public JSONObject deleteUserInformation(@PathVariable("username") String username, Authentication authentication){

        //检查权限
        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        //获取user
        User user = userService.getUserByUsername("username");

        //获取id,因为可能要删除doctor中的数据，所以获取id是必要的
        Integer userId = user.getId();

        //判断被删除用户是否存在
        if (userId == null)
            return CommonUtil.errorJson(ErrorEnum.E_601);

        //判断是否要先将doctor表中的数据删除
        if(DOCTOR_TYPE_LIST.contains(user.getTypeId()) == true){
            doctorService.deleteById(userId);
        }

        //删除用户表中的信息
        userService.deleteById(userId);

        return CommonUtil.successJson(user);
    }

    /**
     * 修改个人信息（个人级别）
     * @param jsonObject
     * @param authentication
     * @return
     */
    @PostMapping("/modify")
    public JSONObject modifyUserInformation(@RequestBody JSONObject jsonObject, Authentication authentication){


        User user = jsonObject.toJavaObject(jsonObject,User.class);
        //是否是个人账号
        try {
            PermissionCheck.isIndivual(authentication, user.getUsername());
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }
        user.setId(PermissionCheck.getIdByUser(authentication));

        return updateMessage(user,jsonObject);
    }


    /**
     * 超级管理员修改信息
     * @param jsonObject 传入的数据比个人传入多一个id
     * @param authentication
     * @return
     */
    @PostMapping("/adminModify")
    public JSONObject adminModifyUserInformation(JSONObject jsonObject, Authentication authentication) {

        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        User user = jsonObject.toJavaObject(jsonObject,User.class);

        return updateMessage(user,jsonObject);
    }

    /**
     * 更新消息（具体方法）
     * @param user
     * @param jsonObject
     * @return
     */
    private JSONObject updateMessage(User user, JSONObject jsonObject){
        //判断用户名是否重复
        if(userService.getUserByUsername(user.getUsername()) == null)
            return CommonUtil.errorJson(ErrorEnum.E_600);

        //判断type_id是否正确
        if(!USER_TYPE_LIST.contains(user.getTypeId()))
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("用户类型"));

        //判断user的身份证号是否正确
        if (user.getIdentifyId().length() != 18)
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("身份信息"));

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.update(user);

        user = userService.getUserByUsername(user.getUsername());

        //修改医生信息
        if (DOCTOR_TYPE_LIST.contains(user.getTypeId())){
            Doctor doctor = jsonObject.toJavaObject(jsonObject,Doctor.class);

            //判断医生职称是否正确
            if (!DOCTOR_TITLE_TYPE_LIST.contains(doctor.getTitleId())) {
                return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("医生职称"));
            }
            doctor.setId(user.getId());
            doctorService.update(doctor);
        }

        return CommonUtil.successJson(user);
    }

    /**
     * 查询个人信息（个人级别）
     * @param username
     * @param authentication
     * @return
     */
    @GetMapping("/selectUser/{username}")
    public JSONObject selectUserInformation(@PathVariable("username") String username, Authentication authentication){

        //是否是个人账号
        try {
            PermissionCheck.isIndivual(authentication, username);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        User user = userService.getUserByUsername(username);

        if (user == null)
            return CommonUtil.errorJson(ErrorEnum.E_601);

        if (DOCTOR_TYPE_LIST.contains(user.getTypeId()))
            user = userService.getUserAllInformationByName(username);

        return CommonUtil.successJson(user);
    }


    /**
     * 查询个人信息（管理员级别）
     * @param username
     * @param authentication
     * @return
     */
    @GetMapping("/adminSelectUser/{username}")
    public JSONObject adminSelectUserInformation(@PathVariable("username") String username, Authentication authentication){

        try {
            PermissionCheck.isHosptialAdim(authentication);
        }catch (Exception e){
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }
        User user = userService.getUserByUsername(username);

        if (user == null)
            return CommonUtil.errorJson(ErrorEnum.E_601);

        if (DOCTOR_TYPE_LIST.contains(user.getTypeId()))
            user = userService.getUserAllInformationByName(username);

        return CommonUtil.successJson(user);
    }

}
