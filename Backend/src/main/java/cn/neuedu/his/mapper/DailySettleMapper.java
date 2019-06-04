package cn.neuedu.his.mapper;

import cn.neuedu.his.model.DailySettle;
import cn.neuedu.his.util.inter.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public interface DailySettleMapper extends MyMapper<DailySettle> {
    DailySettle selectLastTime(@Param("adminId") Integer adminId);
    ArrayList<DailySettle> selectNotCheckByAdminId(@Param("adminId") Integer adminId);
}