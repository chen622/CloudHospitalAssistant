package cn.neuedu.his.util;

import cn.neuedu.his.util.constants.Constants;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;

import java.util.Map;

import static cn.neuedu.his.util.constants.Constants.*;

/**
 * 权限校验类
 */
public class PermissionCheck {
    /**
     * 通用的权限校验接口，所有类别都可通过
     *
     * @param authentication
     * @return
     */
    public static Integer getIdByUser(Authentication authentication) {
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        return (Integer) data.get("id");
    }

    /**
     * 收费员权限检验
     *
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    public static Integer getIdByPaymentAdmin(Authentication authentication) throws AuthenticationServiceException {
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("typeId");
        if (typeId.equals(Constants.UserType.REGISTRATION_CLERK.getId())) {
            return (Integer) data.get("id");
        } else {
            throw new AuthenticationServiceException("");
        }
    }

    /**
     * 收费员或药房操作员（可以生成账单）
     *
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    public static Integer getIdByAdminProducePayment(Authentication authentication) throws AuthenticationServiceException {
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("typeId");
        if (typeId.equals(Constants.UserType.REGISTRATION_CLERK.getId()) || typeId.equals(Constants.UserType.PHARMACY_OPERATOR.getId())) {
            return (Integer) data.get("id");
        } else {
            throw new AuthenticationServiceException("");
        }
    }

    /**
     * 门诊医生权限检验
     *
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    public static Integer isOutpatientDoctor(Authentication authentication) throws AuthenticationServiceException {
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("typeId");
        if (typeId.equals(Constants.UserType.OUT_PATIENT_DOCTOR.getId())) {
            return (Integer) data.get("id");
        } else {
            throw new AuthenticationServiceException("is not Outpatient Doctor ");
        }
    }

    /**
     * 医院管理员权限检验
     *
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    public static Integer isHosptialAdim(Authentication authentication) throws AuthenticationServiceException {
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("typeId");
        if (typeId.equals(UserType.HOSPITAL_ADMINISTRATOR)) {
            return (Integer) data.get("id");
        } else {
            throw new AuthenticationServiceException("");
        }
    }

    /**
     * 个人权限检验
     *
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    public static String isIndivual(Authentication authentication, String username) throws AuthenticationServiceException {
        if (authentication.getName().equals(username)) {
            return (String) authentication.getName();
        } else {
            throw new AuthenticationServiceException("");
        }
    }

    /**
     * 药房管理员权限检验
     *
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    public static Integer getIdByDrugAdmin(Authentication authentication) throws AuthenticationServiceException {
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("typeId");
        if (typeId.equals(UserType.PHARMACY_OPERATOR.getId())) {
            return (Integer) data.get("id");
        } else {
            throw new AuthenticationServiceException("");
        }
    }

    /**
     * 财务管理员权限检验
     *
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    public static Integer isFinancialOfficer(Authentication authentication) throws AuthenticationServiceException {
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("typeId");
        if (typeId.equals(UserType.FINANCIAL_MANAGER.getId())) {
            return (Integer) data.get("id");
        } else {
            throw new AuthenticationServiceException("");
        }
    }


}
