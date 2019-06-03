package cn.neuedu.his.service;
import cn.neuedu.his.model.Drug;
import cn.neuedu.his.util.inter.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface DrugService extends Service<Drug> {
    void takeDrug(Integer paymentId, Integer drugId, Integer drugOperatorId);
}
