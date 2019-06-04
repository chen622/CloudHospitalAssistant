package cn.neuedu.his.service;

import cn.neuedu.his.model.DailySettle;
import cn.neuedu.his.util.inter.Service;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * Created by ccm on 2019/06/04.
 */
public interface DailySettleService extends Service<DailySettle> {
    void makeSettleTable(Integer makeId, Integer adminId, Date endDate) throws IllegalArgumentException;
    ArrayList<DailySettle> getSettleInfo(Integer admin);
    void checkSettleTable(Integer checkId, Integer settleId) throws IllegalArgumentException;
}
