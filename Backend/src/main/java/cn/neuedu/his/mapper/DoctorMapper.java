package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Doctor;
import cn.neuedu.his.model.User;
import cn.neuedu.his.util.inter.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public interface DoctorMapper extends MyMapper<Doctor> {
    Integer getDeptNo(Integer id);
    ArrayList<User> getAllClinicNotDelete();
    List<Doctor> getByDepartmentId(Integer departmentId);
    Integer getDepartmentRegistrationAmountByState(@Param("doctorIdList") ArrayList<Integer> doctorIdList, @Param("startDate") Date startDate, @Param("endDate")Date endDate, @Param("state") Integer state);
}