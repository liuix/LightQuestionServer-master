package com.zyh.lightquestionserver.controller;

import com.zyh.lightquestionserver.entity.User;
import com.zyh.lightquestionserver.server.RedisService;
import com.zyh.lightquestionserver.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    RedisService redisService;

    /**
     * 登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public User login(@RequestBody User user) {
        if("admin".equals(user.getUsername()) && "123".equals(user.getPassword())) {
            user.setToken(JWTUtil.createToken("", user.getUsername()));
            return user;
        }
        return null;
    }

    /**
     * 检查Token是否有效
     * @param request
     * @return
     */
    @GetMapping("/checkToken")
    public Boolean checkToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        return JWTUtil.checkToken(token);
    }
}
