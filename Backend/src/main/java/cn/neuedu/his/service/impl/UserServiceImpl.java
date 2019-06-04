package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.UserMapper;
import cn.neuedu.his.model.User;
import cn.neuedu.his.service.DepartmentService;
import cn.neuedu.his.service.UserService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * Created by ccm on 2019/05/24.
 */
@Service
public class UserServiceImpl extends AbstractService<User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DepartmentService departmentService;

    public User getUserByUsername(String username){
        return userMapper.getUserByUsername(username);
    }

    public void insertUserOperation(User user){
        //判断身份证信息长度
        /*
        if (user.getIdentifyId().length() != 18){
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("身份信息"));}

        //
        if(this.getUserByUsername(user.getUsername()) != null)
            return CommonUtil.errorJson(ErrorEnum.E_600);

        //判断部门是否存在
        Department department = departmentService.findById(user.getDepartmentId());
        if (department == null) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("部门"));
        }

        Integer typeId = user.getTypeId();
        //判断输入type_id是否正确
        if (!USERTYPELIST.contains(typeId))
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("用户类别"));

        //储存user数据
        this.save(user);
         */

    }

    @Override
    public User getUserAllInformationByName(String username){
        return userMapper.getUserAllInformationByUsername(username);
    }

    @Override
    public User findUserAndInvoiceAndPaymentDuringDate(Integer userId, Date startDate, Date endDate) {
        return userMapper.getUserAndInvoiceAndPaymentDuringDate(userId, startDate, endDate);
    }

}
