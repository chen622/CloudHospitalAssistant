package cn.neuedu.his.controller;

import cn.neuedu.his.model.JobSchedule;
import cn.neuedu.his.model.Patient;
import cn.neuedu.his.model.Registration;
import cn.neuedu.his.service.JobScheduleService;
import cn.neuedu.his.service.PatientService;
import cn.neuedu.his.service.RegistrationService;
import cn.neuedu.his.service.UserService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static cn.neuedu.his.util.constants.Constants.registrationClek;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;
    @Autowired
    PatientService patientService;
    @Autowired
    JobScheduleService jobScheduleService;

    /**
     * 现场挂号
     * @param jsonObject
     * @return
     */
    @Transactional
    @PostMapping("/registration")
    public JSONObject registration(@RequestBody JSONObject jsonObject, Authentication authentication) {
        //获取挂号收费员id
        Integer registrarId;
        try {
            registrarId = PermissionCheck.getIdByRegistrar(authentication);
        }catch (AuthenticationServiceException e) {
            return CommonUtil.errorJson(ErrorEnum.E_502);
        }

        //获取挂号信息
        Registration registration = new Registration();
        registration.setRegistrarId(registrarId);
        Patient patient = patientService.findById(jsonObject.getInteger("patientId"));
        if (patient == null)
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("patientId"));

        registration.setPatientId(patient.getId());
        //获取患者年龄
        try {
            registration.setAge(identityIdTransferToAge(patient.getIdentityId()));
        }catch (ParseException | InvalidParameterException e) {
            CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("IdentityId"));
        }

        //获取时间表内信息
        JobSchedule schedule = jobScheduleService.findById(jsonObject.getInteger("scheduleId"));
        if (schedule == null)
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("scheduleId"));

        registration.setScheduleId(schedule.getId());
        registration.setDoctorId(schedule.getDoctorId());
        registration.setState(registrationClek);
        registration.setNeedBook(jsonObject.getBoolean("needBook"));

        registrationService.save(registration);
        return CommonUtil.successJson();
    }

    /**
     * 通过身份证获取年龄
     * @param identifyId
     * @return
     * @throws ParseException
     * @throws InvalidParameterException
     */
    private Integer identityIdTransferToAge(String identifyId) throws ParseException, InvalidParameterException {
        //通过身份证获取生日
        String birthdayStr = identifyId.substring(6, 14);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date birthday;
        try {
            birthday = format.parse(birthdayStr);
        } catch (ParseException e) {
            throw e;
        }
        System.out.println(birthday);

        ////通过生日获取年龄
        Calendar calendar = Calendar.getInstance();
        if(calendar.getTimeInMillis() - birthday.getTime() < 0L)
            throw new InvalidParameterException();

        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(birthday);
        int yearBirthday = calendar.get(Calendar.YEAR);
        int monthBirthday = calendar.get(Calendar.MONTH);
        int dayOfMonthBirthday = calendar.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirthday;

        if (monthNow < monthBirthday || (monthNow == monthBirthday && dayOfMonthNow < dayOfMonthBirthday)) {
            age--;
        }

        return age;
    }



}
