package cn.neuedu.his.mapper;

import cn.neuedu.his.model.DailySettle;
import cn.neuedu.his.util.inter.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

@Component
public interface DailySettleMapper extends MyMapper<DailySettle> {
    Date selectLastTime(@Param("adminId") Integer adminId);
    ArrayList<DailySettle> selectNotCheckByAdminId(@Param("adminId") Integer adminId);
    ArrayList<DailySettle> getByAdminId(@Param("adminId") Integer adminId, @Param("start") Date start, @Param("end") Date end);
}