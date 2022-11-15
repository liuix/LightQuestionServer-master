package com.zyh.lightquestionserver.server.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teautil.models.RuntimeOptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zyh.lightquestionserver.config.ALiYunSMSConfig;
import com.zyh.lightquestionserver.config.SMVCodeConfig;
import com.zyh.lightquestionserver.server.RedisService;
import com.zyh.lightquestionserver.server.SMSConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Slf4j
@Service
public class SMSConfigServiceImpl implements SMSConfigService {
    //阿里云短信服务配置类
    @Autowired
    ALiYunSMSConfig aLiYunSMSConfig;
    @Autowired
    SMVCodeConfig smvCodeConfig;
    @Autowired
    RedisService redisService;

    /**
     * 发送短信
     * @param sendSmsRequest
     * @return
     * @throws Exception
     */
    public SendSmsResponse sendShortMessage(SendSmsRequest sendSmsRequest) throws Exception {
        //初始化配置信息
        Client client = aLiYunSMSConfig.createClient();
        RuntimeOptions runtime = new RuntimeOptions();

        SendSmsResponse sendSmsResponse;
        try {
            //发送短信
            sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, runtime);
        }catch (Exception e){
            throw new Exception("调用阿里云发送短信接口失败",e);
        }
        log.info("调用阿里云发送短信接口成功");
        return sendSmsResponse;
    }

    /**
     * 发送短信验证码
     *
     * @param phoneNumber 手机号
     * @return
     */
    public SendSmsResponse sendSMVCode(String phoneNumber) {
        //生成六位手机验证码
        String verificationCode = this.randomCode();
        //拼接阿里云短信模板变量对应的实际值"{\"code\":\"+verificationCode+\"}";
        HashMap hashMap = new HashMap();
        hashMap.put("code", verificationCode);
        String templateParam = null;
        try {
            templateParam = new ObjectMapper().writeValueAsString(hashMap);
        } catch (JsonProcessingException e) {
            return null;
        }
        //配置发送阿里云短信的请求体
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        //设置短信签名名称
        sendSmsRequest.setSignName(smvCodeConfig.getSignName());
        //设置短信模板Code
        sendSmsRequest.setTemplateCode(smvCodeConfig.getTemplateCode());
        //设置发送短信的手机号
        sendSmsRequest.setPhoneNumbers(phoneNumber);
        //设置短信模板变量对应的实际值
        sendSmsRequest.setTemplateParam(templateParam);
        //发送短信响应体
        SendSmsResponse sendSmsResponse = null;
        try {
            //调用阿里云短信服务发送短信验证码
            sendSmsResponse = this.sendShortMessage(sendSmsRequest);
        } catch (Exception e) {
            log.error("调用阿里云短信服务发送短信验证码接口失败！", e);
            return null;
        }
        if (sendSmsResponse != null && !sendSmsResponse.getBody().getCode().equals("OK")) {
            log.error("调用阿里云短信服务发送短信验证码失败 {}", sendSmsResponse);
            return null;
        }
        saveSMVCode(phoneNumber, verificationCode);
        return sendSmsResponse;
    }

    /**
     * 保存到Redis
     * @param phoneNumber
     * @param verificationCode
     */
    public void saveSMVCode (String phoneNumber, String verificationCode) {
        redisService.set(phoneNumber, verificationCode, 5*60*1000L);
    }

    /**
     * 生成随机六位验证码
     * @return
     */
    public String randomCode() {
        StringBuilder stringBuffer = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            stringBuffer.append(random.nextInt(10));
        }
        return stringBuffer.toString();
    }
}
