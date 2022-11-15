package com.zyh.lightquestionserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    @TableId(type = IdType.ASSIGN_ID)   //雪花算法生成ID，必须使用Long而不是long！
    private Long id;
    private String username;            //用户名
    private String password;            //密码
    private String phone;               //手机号
    private Date date;                  //注册时间
    private String token;               //JWT生成的token
}
