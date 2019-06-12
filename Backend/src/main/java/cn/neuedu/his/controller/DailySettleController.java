package cn.neuedu.his.controller;

import cn.neuedu.his.model.DailySettle;
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

/**
 *
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

    @PostMapping("/makeTable")
    public JSONObject makeDailySettleTable(@RequestBody JSONObject jsonObject, Authentication authentication) {
        Integer maker;
        try {
            maker = PermissionCheck.getIdByPaymentAdmin(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }

        try {
            dailySettleService.makeSettleTable(maker, jsonObject.getInteger("admin"), jsonObject.getDate("endDate"));
        }catch (IllegalArgumentException e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }

        return CommonUtil.successJson();
    }

    @GetMapping("/getSettleInfo/{adminId}")
    public JSONObject getSettleInfo(@PathVariable("adminId") Integer adminId, Authentication authentication) {
        JSONObject result = new JSONObject();
        try {
            PermissionCheck.isFinancialOfficer(authentication);
        }catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        ArrayList<DailySettle> dailySettleList = dailySettleService.getSettleInfo(adminId);
        if (dailySettleList.isEmpty())
            return CommonUtil.successJson(new ArrayList<DailySettle>());

        JSONArray resultArray = new JSONArray();
        for (DailySettle dailySettle: dailySettleList) {
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

    @PostMapping("/check/{settleId}")
    public JSONObject checkPass(@PathVariable("settleId") Integer settleId, Authentication authentication) {
        Integer check;
        try {
            check = PermissionCheck.isFinancialOfficer(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        } catch (Exception e) {
            return CommonUtil.errorJson(ErrorEnum.E_802);
        }

        try {
            dailySettleService.checkSettleTable(check, settleId);
        }catch (IllegalArgumentException e) {
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(e.getMessage()));
        }

        return CommonUtil.successJson();
    }

}
