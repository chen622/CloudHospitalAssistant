package cn.neuedu.his.service;

import cn.neuedu.his.model.DailySettle;
import cn.neuedu.his.util.inter.Service;
import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * Created by ccm on 2019/06/04.
 */
public interface DailySettleService extends Service<DailySettle> {
    DailySettle produceSettleInfo(Integer makeId, Integer adminId, Date end) throws IndexOutOfBoundsException;
    void makeSettleTable(DailySettle dailySettle);
    ArrayList<DailySettle> getSettleInfo(Integer admin);
    void checkSettleTable(Integer checkId, Integer settleId) throws IllegalArgumentException;
    JSONArray getDailyHistory(Integer dailySettleId);
    ArrayList<DailySettle> findByAdminId(Integer adminId, Date start, Date end);
}
