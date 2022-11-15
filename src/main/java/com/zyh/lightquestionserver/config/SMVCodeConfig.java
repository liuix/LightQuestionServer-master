package com.zyh.lightquestionserver.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class SMVCodeConfig {
    //短信签名名称
    @Value("${aliyun.sms.sMVCode.signName}")
    private String signName;
    //短信模板CODE
    @Value("${aliyun.sms.sMVCode.templateCode}")
    private String templateCode;
    //短信模板变量对应的实际值
    private String templateParam;
    //短信验证码存储在redis中的时间 单位分钟
    @Value("${aliyun.sms.sMVCode.limitTime}")
    private String limitTime;
}