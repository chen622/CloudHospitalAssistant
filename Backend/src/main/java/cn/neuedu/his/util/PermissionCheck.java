package cn.neuedu.his.util;

import cn.neuedu.his.util.constants.Constants;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;

import java.util.Map;

import static cn.neuedu.his.util.constants.Constants.FINANCIAL_MANAGER;
import static cn.neuedu.his.util.constants.Constants.REGISTRATION_CLERK;

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
        Integer typeId = (Integer) data.get("typeId");
        return (Integer) data.get("id");
    }

    /**
     * 通用的权限校验接口，所有类别都可通过
     *
     * @param authentication
     * @return
     */
    public static Integer getNameByUser(Authentication authentication) {
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("username");
        return (Integer) data.get("username");
    }

    /**
     * 挂号员权限检验
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    public static Integer getIdByRegistrar(Authentication authentication) throws AuthenticationServiceException{
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("typeId");
        if (typeId.equals(REGISTRATION_CLERK)) {
            return (Integer) data.get("id");
        } else {
            throw new AuthenticationServiceException("");
        }
    }

    /**
     * 门诊医生权限检验
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    public static Integer isOutpatientDoctor(Authentication authentication) throws AuthenticationServiceException{
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("typeId");
        if (typeId.equals(Constants.OUT_PATIENT_DOCTOR)) {
            return (Integer) data.get("id");
        } else {
            throw new AuthenticationServiceException("");
        }
    }

    /**
     * 医院管理员权限检验
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    public static Integer isHosptialAdim(Authentication authentication) throws AuthenticationServiceException{
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("typeId");
        if (typeId.equals(Constants.HOSPITAL_ADMIN)) {
            return (Integer) data.get("id");
        } else {
            throw new AuthenticationServiceException("");
        }
    }

    /**
     * 个人权限检验
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    public static String isIndivual(Authentication authentication, String username) throws AuthenticationServiceException{
        if (authentication.getName().equals(username)) {
            return (String) authentication.getName();
        } else {
            throw new AuthenticationServiceException("");
        }
    }

    public static Integer canPrintInvoice(Authentication authentication) throws AuthenticationServiceException{
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("typeId");
        if (typeId.equals(REGISTRATION_CLERK)|| typeId.equals(FINANCIAL_MANAGER)) {
            return (Integer) data.get("id");
        } else {
            throw new AuthenticationServiceException("");
        }
    }


}
