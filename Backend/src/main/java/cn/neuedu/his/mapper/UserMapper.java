package cn.neuedu.his.mapper;

import cn.neuedu.his.model.User;
import cn.neuedu.his.util.inter.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component

public interface UserMapper extends MyMapper<User> {
    User getUserByUsername(String username);
    User getUserAndInvoiceAndPaymentDuringDate(@Param("userId") Integer userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    User getUserAllInformationByUsername(String username);

}