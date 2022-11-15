package com.zyh.lightquestionserver.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class JWTUtil {

    private static final long time = 1000*60*60*24;                     //有效时间
    private static final String signature = "SDZFXYwlkjaqxyTMS4548";    //签名

    /**
     * 创建Token
     * @param role 载荷内容
     * @param name 用户名
     * @return jwtToken
     */
    public static String createToken(String role, String name) {
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwtToken = jwtBuilder
                //header    头部
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                //payload   载荷
                .claim("name",name)
                .claim("role",role)
                //主题
                .setSubject("admin")
                //有效期
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .setId(UUID.randomUUID().toString())
                //signature签名
                .signWith(SignatureAlgorithm.HS256,signature)
                .compact();
        return jwtToken;
    }

    /**
     * 检查Token是否有效
     * @param token token
     * @return bool
     */
    public static boolean checkToken(String token){
        if (Objects.equals(token, "") || token == null){
            return false;
        }

        try {
            //解析后拿到存有token信息的集合
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(signature).parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
