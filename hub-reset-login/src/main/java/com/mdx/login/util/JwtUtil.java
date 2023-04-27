/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mdx.login.util;

import com.mdx.login.constant.TokenConstant;
import com.mdx.login.vo.TokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * JwtUtil
 *
 * @author Chill
 */
public class JwtUtil {

	public static String SIGN_KEY = TokenConstant.SIGN_KEY;
	public static String BEARER = TokenConstant.BEARER;
	public static Integer AUTH_LENGTH = 7;

	public static String BASE64_SECURITY = Base64.getEncoder().encodeToString(SIGN_KEY.getBytes(StandardCharsets.UTF_8));

	/**
	 * 获取token串
	 *
	 * @param auth token
	 * @return String
	 */
	public static String getToken(String auth) {
		if ((auth != null) && (auth.length() > AUTH_LENGTH)) {
			String headStr = auth.substring(0, 6).toLowerCase();
			if (headStr.compareTo(BEARER) == 0) {
				auth = auth.substring(7);
			}
			return auth;
		}
		return null;
	}

	/**
	 * 解析jsonWebToken
	 *
	 * @param jsonWebToken token串
	 * @return Claims
	 */
	public static Claims parseJWT(String jsonWebToken) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(Base64.getDecoder().decode(BASE64_SECURITY)).build()
				.parseClaimsJws(jsonWebToken).getBody();
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 生成token
	 */
	public static TokenInfo createJWT(Map<String, Object> param, String audience, String issuer, String tokenType) {

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		//生成签名密钥
		byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE64_SECURITY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
				.setIssuer(issuer)
				.setAudience(audience)
				.signWith(signingKey);
		param.forEach(builder::claim);
		//设置应用id
		builder.claim(TokenConstant.CLIENT_ID, "clientId");

		//添加Token过期时间
		long expireMillis;
		if (tokenType.equals(TokenConstant.ACCESS_TOKEN)) {
			expireMillis = 300 * 1000;
		} else if (tokenType.equals(TokenConstant.REFRESH_TOKEN)) {
			expireMillis = 600 * 1000;
		} else {
			expireMillis = getExpire();
		}
		long expMillis = nowMillis + expireMillis;
		Date exp = new Date(expMillis);
		builder.setExpiration(exp).setNotBefore(now);

		// 组装Token信息
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setToken(builder.compact());
		tokenInfo.setExpire((int) expireMillis / 1000);
		return tokenInfo;
	}


	/**
	 * 获取过期时间(次日凌晨3点)
	 *
	 * @return expire
	 */
	public static long getExpire() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 1);
		cal.set(Calendar.HOUR_OF_DAY, 3);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis() - System.currentTimeMillis();
	}

}
