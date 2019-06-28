package cn.neuedu.his.mapper;

import cn.neuedu.his.model.NonDrug;
import cn.neuedu.his.model.PaymentType;
import cn.neuedu.his.util.inter.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface NonDrugMapper extends MyMapper<NonDrug> {

    NonDrug selectNonDrugByName(String name);

    NonDrug selectNonDrugByCode(String code);

    List<NonDrug> findByName(String name);

    List<NonDrug> getAll();

    List<PaymentType> getTypeAndNonDrug(String name, String code, Boolean auth);

    List<PaymentType> getNonDrugByType(@Param("auth") Boolean auth);

    List<NonDrug> getNonDrugByDepartmentId(Integer id);
}