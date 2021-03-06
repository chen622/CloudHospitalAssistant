package cn.neuedu.his.util;

import cn.neuedu.his.model.Doctor;
import cn.neuedu.his.service.DoctorService;
import cn.neuedu.his.service.impl.RedisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 权限校验类
 */
@Component
public class PermissionCheck {
    @Autowired
    RedisServiceImpl redisServiceTemp;

    private static RedisServiceImpl redisService;

    @PostConstruct
    public void init() {
        redisService = redisServiceTemp;
    }

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
     * 患者的权限校验接口
     *
     * @param authentication
     * @return
     */
    public static Integer getIdByPatient(Authentication authentication) throws AuthenticationServiceException {
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        if ((Integer) data.get("typeId") != -1) {
            throw new AuthenticationServiceException("");
        } else {
            return (Integer) data.get("id");
        }
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
        try {
            Map<String, Integer> map = redisService.getMapAll("userType");
            if (typeId.equals(map.get("挂号收费员"))) {
                Integer id = (Integer) data.get("id");
                if (id == null)
                    throw new Exception();
                else
                    return id;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new AuthenticationServiceException("");
        }


    }

    /**
     * 收费员权限检验
     *
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    public static Integer getIdByPaymentAdminAndHospitalAdmin(Authentication authentication) throws AuthenticationServiceException {
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("typeId");
        try {
            Map<String, Integer> map = redisService.getMapAll("userType");
            if (typeId.equals(map.get("挂号收费员")) || typeId.equals(map.get("医院管理员"))) {
                Integer id = (Integer) data.get("id");
                if (id == null)
                    throw new Exception();
                else
                    return id;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
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
    public static Integer getIdByAdminProducePayment(Authentication authentication) throws Exception {
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("typeId");
        Map<String, Integer> map = redisService.getMapAll("userType");

        if (typeId.equals(map.get("挂号收费员")) || typeId.equals(map.get("药房操作员"))) {
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
        Map<String, Integer> map = null;
        try {
            map = redisService.getMapAll("userType");
            if (typeId.equals(map.get("门诊医生"))) {
                Integer id = (Integer) data.get("id");
                if (id == null)
                    throw new Exception();
                else
                    return id;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new AuthenticationServiceException("");
        }
    }


    /**
     * 主任医师权限检验
     *
     * @return
     * @throws AuthenticationServiceException
     */
    public static boolean isChiefDoctor(Integer titleId) throws AuthenticationServiceException {
        try {
            Map<String, Integer> map = redisService.getMapAll("title");
            if (titleId.equals(map.get("主任医师"))) {
                return true;
            } else {
                throw new AuthenticationServiceException("ChiefDoctor");
            }
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage());
        }

    }


    /**
     * 副主任医师以及以上权限检验
     *
     * @param titleId
     * @return
     * @throws AuthenticationServiceException
     */
    public static boolean aboveDeputyChiefDoctor(Integer titleId) throws AuthenticationServiceException {
        try {
            Map<String, Integer> map = redisService.getMapAll("title");

            if (titleId.equals(map.get("副主任医师")) || titleId.equals(map.get("主任医师"))) {
                return true;
            } else {
                throw new AuthenticationServiceException("DeputyChiefDocto");
            }
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
    }


    /**
     * 主治医师以及以上权限检验
     *
     * @param titleId
     * @return
     * @throws AuthenticationServiceException
     */
    public static boolean aboveATTENDING_DOCTOR(Integer titleId) throws AuthenticationServiceException {
        try {
            Map<String, Integer> map = redisService.getMapAll("title");

            if (titleId.equals(map.get("副主任医师")) || titleId.equals(map.get("主任医师")) || titleId.equals(map.get("主治医师"))) {
                return true;
            } else {
                throw new AuthenticationServiceException("ATTENDING_DOCTOR");
            }
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
    }


    /**
     * 医院管理员权限检验
     *
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    public static Integer isHospitalAdmin(Authentication authentication) throws AuthenticationServiceException {
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("typeId");
        Map<String, Integer> map = null;
        try {
            map = redisService.getMapAll("userType");
            if (typeId.equals(map.get("医院管理员"))) {
                Integer id = (Integer) data.get("id");
                if (id == null)
                    throw new Exception();
                else
                    return id;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new AuthenticationServiceException("");
        }
    }

    /**
     * 医院管理员权限检验
     * 异常类型返回用户Id
     *
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    public static Integer isHosptialAdimReturnUserId(Authentication authentication) throws AuthenticationServiceException {
        try {
            Integer userId = isHospitalAdmin(authentication);
            return userId;
        } catch (Exception e) {
            Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
            Integer typeId = (Integer) data.get("id");
            throw new AuthenticationServiceException(typeId.toString());
        }
    }

    /**
     * 医院管理员权限检验
     *
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    public static Integer isTechnicalDoctor(Authentication authentication) throws Exception {
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("typeId");
        Map<String, Integer> map = redisService.getMapAll("userType");
        if (typeId.equals(map.get("医技医生"))) {
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
    public static Integer getIdByDrugAdmin(Authentication authentication) throws Exception {
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("typeId");
        Map<String, Integer> map = redisService.getMapAll("userType");
        if (typeId.equals(map.get("药房操作员"))) {
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
        Map<String, Integer> map = null;
        try {
            map = redisService.getMapAll("userType");
            if (typeId.equals(map.get("财务管理员"))) {
                return (Integer) data.get("id");
            } else {
                throw new AuthenticationServiceException("");
            }
        } catch (Exception e) {
            throw new AuthenticationServiceException("redis");
        }
    }

}
