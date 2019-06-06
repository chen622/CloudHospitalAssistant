package cn.neuedu.his.controller;

import cn.neuedu.his.model.Department;
import cn.neuedu.his.model.Doctor;
import cn.neuedu.his.model.User;
import cn.neuedu.his.service.DepartmentService;
import cn.neuedu.his.service.DoctorService;
import cn.neuedu.his.service.UserService;
import cn.neuedu.his.service.impl.RedisServiceImpl;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.Constants;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.Inet4Address;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    class url {
        String name;
        String url;
        String key;

        url(String name, String url, String key) {
            this.name = name;
            this.url = url;
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public String getKey() {
            return key;
        }
    }

    @Autowired
    UserService userService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RedisServiceImpl redisService;

    @GetMapping("/function")
    public JSONObject login(Authentication authentication) throws Exception {
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("typeId");
        JSONArray urls = new JSONArray();
        Map<String, Integer> map = redisService.getMapAll("userType");
        if (typeId.equals(map.get("医院管理员"))) {
            urls.add(new url("账户管理", "/admin/user", "admin"));
            urls.add(new url("排班管理", "/admin/rule", "rule"));
            urls.add(new url("医疗信息管理", "/admin/other", "other"));
        }else if (typeId.equals(map.get("门诊医生"))){
            urls.add(new url("看诊","/doctor/index","doctor"));
        }
        return CommonUtil.successJson(urls);
    }

    /**
     * 注册用户信息
     *
     * @param jsonObject 用户信息
     * @return 是否成功
     */
    @PostMapping("/register")
    public JSONObject register(@RequestBody JSONObject jsonObject) throws Exception {

        User user = JSONObject.toJavaObject(jsonObject, User.class);

        //判断身份证信息长度
        if (user.getIdentifyId().length() != 18) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("身份信息"));
        }

        //创建用户对象
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //
        if (userService.getUserByUsername(user.getUsername()) != null)
            return CommonUtil.errorJson(ErrorEnum.E_600);

        //判断部门是否存在
        Department department = departmentService.findById(user.getDepartmentId());
        if (department == null) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("部门"));
        }

        Integer typeId = user.getTypeId();

        Map<String ,Integer> map=redisService.getMapAll("userType");

        //判断输入type_id是否正确
        if (!map.containsValue(typeId))
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("用户类别"));

        //储存user数据
        userService.save(user);

        Map<String , Integer> map2=redisService.getMapAll("doctor");

        //类别属于医生
        if (map2.containsValue(typeId)) {
            //取得当前user的id
            Integer id = userService.getUserByUsername(user.getUsername()).getId();

            //创立医生对象
            Doctor doctor = JSONObject.toJavaObject(jsonObject, Doctor.class);
            //判断医生职称类型是否正确

            Map<String ,Integer> title=redisService.getMapAll("title");
            if (!title.containsValue(doctor.getTitleId())) {
                return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("医生职称"));
            }

            doctor.setId(id);
            //储存doctor数据
            doctorService.save(doctor);
        }

        return CommonUtil.successJson(user);
    }

    /**
     * 管理员删除信息
     *
     * @param id
     * @param authentication
     * @return
     */
    @PostMapping("/delete/{id}")
    public JSONObject deleteUserInformation(@PathVariable("id") Integer id, Authentication authentication) throws Exception {

        //检查权限
        try {
            PermissionCheck.isHosptialAdim(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        //获取user
        User user = userService.findById(id);

        //判断被删除用户是否存在
        if (user == null)
            return CommonUtil.errorJson(ErrorEnum.E_601);

        Map<String ,Integer> map=redisService.getMapAll("doctor");

        //判断是否要先将doctor表中的数据删除
        if (map.containsValue(user.getTypeId()) == true) {
            Doctor doctor = doctorService.findById(id);
            doctor.setDelete(true);
            doctorService.update(doctor);
        }

        //删除用户表中的信息
        user.setDelete(true);
        userService.update(user);

        return CommonUtil.successJson(user);
    }

    /**
     * 修改个人信息（个人级别）
     *
     * @param jsonObject
     * @param authentication
     * @return
     */
    @PostMapping("/modify")
    public JSONObject modifyUserInformation(@RequestBody JSONObject jsonObject, Authentication authentication) throws Exception {


        User user = JSONObject.toJavaObject(jsonObject, User.class);
        //是否是个人账号
        try {
            PermissionCheck.isIndivual(authentication, user.getUsername());
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }
        user.setId(PermissionCheck.getIdByUser(authentication));

        return updateMessage(user, jsonObject);
    }


    /**
     * 超级管理员修改信息
     *
     * @param jsonObject     传入的数据比个人传入多一个id
     * @param authentication
     * @return
     */
    @PostMapping("/adminModify")
    public JSONObject adminModifyUserInformation(JSONObject jsonObject, Authentication authentication) throws Exception {

        try {
            PermissionCheck.isHosptialAdim(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        User user = JSONObject.toJavaObject(jsonObject, User.class);

        return updateMessage(user, jsonObject);
    }

    /**
     * 更新消息（具体方法）
     *
     * @param user
     * @param jsonObject
     * @return
     */
    private JSONObject updateMessage(User user, JSONObject jsonObject) throws Exception {
        //判断用户名是否重复
        if (userService.getUserByUsername(user.getUsername()) == null)
            return CommonUtil.errorJson(ErrorEnum.E_600);

        Map<String ,Integer> map = null;
        try {
            map=redisService.getMapAll("userType");
        } catch (Exception e) {
            CommonUtil.errorJson(ErrorEnum.E_802);
        }
        if(map==null)
            return   CommonUtil.errorJson(ErrorEnum.E_802);
        //判断type_id是否正确
        if (!map.containsValue(user.getTypeId()))
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("用户类型"));

        //判断user的身份证号是否正确
        if (user.getIdentifyId().length() != 18)
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("身份信息"));

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.update(user);

        user = userService.getUserByUsername(user.getUsername());

        Map<String ,Integer> map2=redisService.getMapAll("doctor");

        //修改医生信息
        if (map2.containsValue(user.getTypeId())) {
            Doctor doctor = jsonObject.toJavaObject(jsonObject, Doctor.class);

            Map<String ,Integer> title=redisService.getMapAll("title");
            //判断医生职称是否正确
            if (!title.containsValue(doctor.getTitleId())) {
                return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("医生职称"));
            }
            doctor.setId(user.getId());
            doctorService.update(doctor);
        }

        return CommonUtil.successJson(user);
    }

    /**
     * 查询个人信息（个人级别）
     *
     * @param username
     * @param authentication
     * @return
     */
    @GetMapping("/selectUser/{username}")
    public JSONObject selectUserInformation(@PathVariable("username") String username, Authentication authentication) throws Exception {

        //是否是个人账号
        try {
            PermissionCheck.isIndivual(authentication, username);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        User user = userService.getUserByUsername(username);

        if (user == null)
            return CommonUtil.errorJson(ErrorEnum.E_601);

        Map<String ,Integer> map=redisService.getMapAll("doctor");

        if (map.containsValue(user.getTypeId()))
            user = userService.getUserAllInformationByName(username);

        return CommonUtil.successJson(user);
    }


    /**
     * 查询个人信息（管理员级别）
     *
     * @param username
     * @param authentication
     * @return
     */
    @GetMapping("/adminSelectUser/{username}")
    public JSONObject adminSelectUserInformation(@PathVariable("username") String username, Authentication authentication) throws Exception {

        try {
            PermissionCheck.isHosptialAdim(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }
        User user = userService.getUserByUsername(username);

        if (user == null)
            return CommonUtil.errorJson(ErrorEnum.E_601);

        Map<String ,Integer> map=redisService.getMapAll("doctor");
        if (map.containsValue(user.getTypeId()))
            user = userService.getUserAllInformationByName(username);

        return CommonUtil.successJson(user);
    }

    /**
     * 模糊搜索用户
     * @param name
     * @param authentication
     * @return
     */
    @GetMapping("/findUser/{name}")
    public JSONObject findUser(@PathVariable("name") String name, Authentication authentication){

        try {
            PermissionCheck.isHosptialAdim(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_602);
        }

        try{
            List<User> users = userService.findUser(name);
            return CommonUtil.successJson(users);
        }catch (RuntimeException e){
            e.printStackTrace();
            return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }

    @GetMapping("/findAll")
    public JSONObject findAll(){
        try{
            List<User> users = userService.findAllWithName();
            return CommonUtil.successJson(users);
        }catch (RuntimeException e){
            e.printStackTrace();
            return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }


}
