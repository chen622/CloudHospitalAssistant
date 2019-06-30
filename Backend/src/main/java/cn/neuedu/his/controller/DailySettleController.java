package cn.neuedu.his.controller;

import cn.neuedu.his.model.DailySettle;
import cn.neuedu.his.model.User;
import cn.neuedu.his.service.DailySettleService;
import cn.neuedu.his.service.InvoiceService;
import cn.neuedu.his.service.UserService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ccm on 2019/06/04.
 */
@RestController
@RequestMapping("/daily_settle")
public class DailySettleController {

    @Autowired
    DailySettleService dailySettleService;
    @Autowired
    InvoiceService invoiceService;
    @Autowired
    UserService userService;

    /**
     * 生成日结单信息
     *
     * @param jsonObject
     * @param authentication
     * @return
     */
    @PostMapping("/produceSettleInfo")
    public JSONObject produceSettleInfo(@RequestBody JSONObject jsonObject, Authentication authentication) {
        Integer maker;
        try {
            maker = PermissionCheck.getIdByPaymentAdmin(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        Integer adminId = jsonObject.getInteger("admin");
        JSONObject result = new JSONObject();
        DailySettle dailySettle;
        try {
            dailySettle = dailySettleService.produceSettleInfo(maker, adminId, jsonObject.getDate("endDate"));
        } catch (IndexOutOfBoundsException e) {
            return CommonUtil.errorJson(ErrorEnum.E_513);
        }
        result.put("dailySettle", dailySettle);
        result.put("maker", userService.findById(maker));

        return CommonUtil.successJson(result);
    }

    /**
     * 保存日结单，并冻结payment
     *
     * @param jsonObject
     * @param authentication
     * @return
     */
    @PostMapping("/makeTable")
    public JSONObject makeDailySettleTable(@RequestBody JSONObject jsonObject, Authentication authentication) {
        try {
            PermissionCheck.getIdByPaymentAdmin(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        dailySettleService.makeSettleTable(jsonObject.getObject("dailySettle", DailySettle.class));

        return CommonUtil.successJson();
    }

    /**
     * 财务管理员获得存储的日结单信息
     *
     * @param adminId
     * @param authentication
     * @return
     */
    @GetMapping("/getSettleInfo/{adminId}")
    public JSONObject getSettleInfo(@PathVariable("adminId") Integer adminId, Authentication authentication) {
        JSONObject result = new JSONObject();
        try {
            PermissionCheck.isFinancialOfficer(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        ArrayList<DailySettle> dailySettleList = dailySettleService.getSettleInfo(adminId);
        if (dailySettleList.isEmpty())
            dailySettleList = new ArrayList<DailySettle>();

        JSONArray resultArray = new JSONArray();
        for (DailySettle dailySettle : dailySettleList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("dailySettle", dailySettle);
            jsonObject.put("normalInvoiceId", invoiceService.getInvoiceNormalIdList(dailySettle.getId()));
            jsonObject.put("anewInvoiceId", invoiceService.getInvoiceAnewIdList(dailySettle.getId()));
            resultArray.add(jsonObject);
        }
        result.put("settleList", resultArray);
        result.put("admin", userService.findById(adminId));

        return CommonUtil.successJson(result);
    }

    /**
     * 核对日结信息
     *
     * @param settleId
     * @param authentication
     * @return
     */
    @PostMapping("/check/{settleId}")
    public JSONObject checkPass(@PathVariable("settleId") Integer settleId, Authentication authentication) {
        Integer check;
        try {
            check = PermissionCheck.isFinancialOfficer(authentication);
        } catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }

        try {
            dailySettleService.checkSettleTable(check, settleId);
        } catch (IllegalArgumentException e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }

        return CommonUtil.successJson();
    }

    /**
     * 获取收费员（初始化页面）
     *
     * @param authentication
     * @return
     */
    @GetMapping("/initialize")
    public JSONObject initializePage(Authentication authentication) {
        Integer maker;
        try {
            maker = PermissionCheck.getIdByPaymentAdmin(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        User user = userService.findById(maker);
        if (user == null)
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("userId"));

        return CommonUtil.successJson(user);
    }

    /**
     * 获取日结历史信息
     * @param dailySettleId
     * @param authentication
     * @return
     */
    @GetMapping("/dailyHistory/{dailySettleId}")
    public JSONObject searchDailyHistory(@PathVariable("dailySettleId") Integer dailySettleId, Authentication authentication) {
        try {
            PermissionCheck.getIdByPaymentAdmin(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        return CommonUtil.successJson(dailySettleService.getDailyHistory(dailySettleId));
    }

    /**
     * 获取收费员所有日结信息
     * @param authentication
     * @return
     */
    @GetMapping("/getAll")
    public JSONObject getAllByAdmin(Authentication authentication) {
        Integer admin;
        try {
            admin = PermissionCheck.getIdByPaymentAdmin(authentication);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        ArrayList<DailySettle> settleList = dailySettleService.findByAdminId(admin);
        if (settleList.isEmpty())
            settleList = null;

        return CommonUtil.successJson(settleList);
    }
}
