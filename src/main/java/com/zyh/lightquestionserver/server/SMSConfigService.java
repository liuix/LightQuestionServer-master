package com.zyh.lightquestionserver.server;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;

public interface SMSConfigService {
    SendSmsResponse sendSMVCode(String phoneNumber);
    SendSmsResponse sendShortMessage(SendSmsRequest sendSmsRequest) throws Exception;
}
