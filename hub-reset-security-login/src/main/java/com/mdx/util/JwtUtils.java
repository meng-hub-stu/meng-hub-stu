package com.mdx.util;

import com.mdx.pojo.Payload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * @author ：YangYaNan
 * @date ：Created in 16:30 2022/4/11
 * @description ：
 * @version: 1.0
 */
public class JwtUtils {

    private static final String JWT_PAYLOAD_USER_KEY = "user";

    public static final String BEARER_PREFIX = "Bearer ";

    public static final int EXPIRE_TIME = 24 * 60;

    /**
     * 私钥加密token
     *
     * @param jwtClaim   载荷中的数据
     * @param privateKey 私钥
     * @param expire     过期时间，单位分钟
     * @return JWT
     */
    public static String generateTokenExpireInMinutes(Object jwtClaim, PrivateKey privateKey, int expire) {
        return Jwts.builder()
                .claim(JWT_PAYLOAD_USER_KEY, GsonSerializeUtils.toString(jwtClaim))
                .setId(createJTI())
                .setExpiration(
                    Date.from(LocalDateTime.now().plusMinutes(expire).atZone(ZoneId.systemDefault()).toInstant())
                )
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    /**
     * 私钥加密token
     *
     * @param userInfo   载荷中的数据
     * @param privateKey 私钥
     * @param expire     过期时间，单位秒
     * @return JWT
     */
    public static String generateTokenExpireInSeconds(Object userInfo, PrivateKey privateKey, int expire) {
        return Jwts.builder()
                .claim(JWT_PAYLOAD_USER_KEY, GsonSerializeUtils.toString(userInfo))
                .setId(createJTI())
                .setExpiration(
                    Date.from(LocalDateTime.now().plusMinutes(expire).atZone(ZoneId.systemDefault()).toInstant())
                )
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    /**
     * 公钥解析token
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @return Jws<Claims>
     */
    private static Jws<Claims> parserToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    private static String createJTI() {
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
    }

    /**
     * 获取token中的用户信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     */
    public static <T> Payload<T> getInfoFromToken(String token, PublicKey publicKey, Class<T> userType) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        Payload<T> claims = new Payload<>();
        claims.setId(body.getId());
        claims.setUserInfo(GsonSerializeUtils.toBean(body.get(JWT_PAYLOAD_USER_KEY).toString(), userType));
        claims.setExpiration(body.getExpiration());
        return claims;
    }

    /**
     * 获取token中的载荷信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     */
    public static <T> Payload<T> getInfoFromToken(String token, PublicKey publicKey) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        Payload<T> claims = new Payload<>();
        claims.setId(body.getId());
        claims.setExpiration(body.getExpiration());
        return claims;
    }
}
