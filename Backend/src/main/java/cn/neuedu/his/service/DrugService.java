package cn.neuedu.his.service;
import cn.neuedu.his.model.ConstantVariable;
import cn.neuedu.his.model.Drug;
import cn.neuedu.his.util.inter.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@org.springframework.stereotype.Service
public interface DrugService extends Service<Drug> {
    void takeDrug(Integer paymentId, Integer drugId, Integer drugOperatorId) throws IllegalArgumentException, UnsupportedOperationException;
    void retreatDrug(Integer paymentId, Integer drugId, Integer quantity, Integer drugOperatorId) throws IllegalArgumentException, UnsupportedOperationException, IndexOutOfBoundsException;

    List<Drug> getDrugByName(String name);

    void deleteDrug(Integer id);

    void modifyDrug(Drug drug) throws Exception;

    Integer insertDrug(Drug drug) throws Exception;

    ArrayList<Drug> getAllDrug();

    ArrayList<Drug> getDrugByPartName(String name);

    List<ConstantVariable> getTypeAndDrugs(Boolean auth);


}
