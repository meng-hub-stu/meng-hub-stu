package com.mdx.login.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mdx.login.util.TokenUtil;
import com.mdx.login.vo.AuthInfo;
import com.mdx.pojo.User;
import com.mdx.service.IUserService;
import com.mdx.util.JsonUtils;
import com.mdx.util.MD5Utils;
import com.mdx.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * cas进行单点登录
 * @Author Mengdexin
 * @date 2021 -12 -19 -14:30
 */
@Controller
@Api(value = "sso校验单点登录", tags = {"sso校验单点登录"})
@AllArgsConstructor
public class SsoController {

    private final IUserService userService;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String REDIS_USER_TOKEN = "redis_user_token";
    private static final String REDIS_USER_TICKET = "redis_user_ticket";
    private static final String REDIS_TMP_TICKET = "redis_tmp_ticket";
    private static final String COOKIE_USER_TICKET = "cookie_user_ticket";

    /**
     * 跳转登录页面
     * @param request
     * @param response
     * @param returnUrl
     * @param model
     * @return
     */
    @GetMapping("/login")
    @ApiOperation(value = "登录页面", notes = "入参")
    public String hello(HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestParam String returnUrl,
                        Model model){
        model.addAttribute("returnUrl", returnUrl);
//       校验是否已经登录了（一个浏览器只有用一个相同的cookie，cookie中存储的是用户的全局票据）
        String userTicket = getCookie(COOKIE_USER_TICKET, request);
        boolean isVerified = verifyUserTicket(userTicket);
        if(isVerified){
            //重新生成一个临时的门票
            String tmpTicket = createTmpTicket();
            return "redirect:" + returnUrl + "?tmpTicket = " + tmpTicket;
        }
        return "login";
    }

    /**
     * 校验cas全局用户门票
     * @param userTicket 用户门票
     * @return
     */
    private boolean verifyUserTicket(String userTicket) {
        if (isBlank(userTicket)) {
            return false;
        }
        String userId = (String) redisTemplate.opsForValue().get(REDIS_USER_TICKET + ":" + userTicket);
        if (isBlank(userId)) {
            return false;
        }
        String userInfo = (String) redisTemplate.opsForValue().get(REDIS_USER_TOKEN + ":" + userId);
        if (isBlank(userInfo)){
            return false;
        }
        return true;
    }

    /**
     * cas进行登录授权 1.用户会话；2.全局票据；3.临时票据；4.设置cookie
     * @param username
     * @param password
     * @param returnUrl
     * @param request
     * @param model
     * @param response
     * @return
     */
    @PostMapping("/doLogin")
    @ApiOperation(value = "进行登录", notes = "进行登录")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String returnUrl,
                          HttpServletRequest request,
                          Model model,
                          HttpServletResponse response){
        model.addAttribute("returnUrl", returnUrl);
        if(isBlank(username) || isBlank(password)){
            model.addAttribute("errmsg", "用户名和密码不能为空");
            return "login";
        }
        //1.验证登录
        User user = null;
        try {
            user = userService.getOne(Wrappers.<User>lambdaQuery()
                    .eq(User::getUserName, username)
                    .eq(User::getPassword, password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(user == null){
            model.addAttribute("errmsg", "用户不存在");
            return "login";
        }
        //生成token
        AuthInfo authInfo = TokenUtil.createAuthInfo(user);
        //存储登录信息
        redisTemplate.opsForValue().set(REDIS_USER_TOKEN + ":" + authInfo.getId(), JsonUtils.objectToJson(authInfo));


        //3.生成ticket的门票，全局门票，代表用户在cas端登陆过
        String userTicket = UUID.randomUUID().toString().trim();
            //3.1用户全局门票需要放在cas端的cookie中
        setCookie(COOKIE_USER_TICKET, userTicket, response);

        //4.userTicket关联用户的id，放在redis中，代表这个用户有门票了。
        redisTemplate.opsForValue().set(REDIS_USER_TICKET + ":" + userTicket, authInfo.getId());

        //5.生成临时的票据，会跳到调用网站，使用cas签发的一个临时的一次性的ticket
        String tmpTicket = createTmpTicket();
        //6.降临时的票据回调给调用网站
//        return "login";
        return "redirect:" + returnUrl + "?tmpTicket=" + tmpTicket;
    }

    /**
     * 验证临时票据，从而获取用户会话信息
     * @param request
     * @param tmpTicket
     * @return
     * @throws Exception
     */
    @RequestMapping("/verifyTmpTicket")
    @ResponseBody
    public R verifyTmpTicket(HttpServletRequest request,
                             String tmpTicket) throws Exception{
        //1.验证临时票据
        if (isBlank(tmpTicket)) {
            return R.errorMsg("临时票据不能为空");
        }
        String redisTmpTicket = (String) redisTemplate.opsForValue().get(REDIS_TMP_TICKET + ":" + tmpTicket);
        if (isBlank(redisTmpTicket)) {
            return R.errorMsg("临时票据已失效，请重新获取");
        }
        if (!redisTmpTicket.equals(MD5Utils.getMD5Str(tmpTicket))){
            return R.errorMsg("临时票据不正确");
        }
        //2.销毁临时票据
        redisTemplate.delete(REDIS_TMP_TICKET + ":" + tmpTicket);

        //3.获取全局票据（在cookie中获取）
        String userTicket = getCookie(COOKIE_USER_TICKET, request);
        if (isBlank(userTicket)) {
            return R.errorMsg("在cookie中获取全局票据异常");
        }

        //4.全局票据获取用户id
        String userId = (String)redisTemplate.opsForValue().get(REDIS_USER_TICKET + ":" + userTicket);
        if (isBlank(userId)){
            return R.errorMsg("全局票据异常");
        }

        //5.换取用户会话
        String userInfo = (String) redisTemplate.opsForValue().get(REDIS_USER_TOKEN + ":" + userId);
        if (isBlank(userInfo)) {
            return R.errorMsg("用户会话异常");
        }
        return R.ok(JsonUtils.jsonToPojo(userInfo, AuthInfo.class));
    }

    private String getCookie(String key, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || isBlank(key)) {
            return null;
        }
        Cookie cookie = Arrays.stream(cookies).filter(v -> v.getName().equals(key)).findFirst().get();
        if (cookie == null){
            return null;
        }
        return cookie.getValue();
    }

    private void setCookie(String key, String val, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, val);
        cookie.setDomain("sso.com");
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    private String createTmpTicket() {
        String tmpTicket = UUID.randomUUID().toString().trim();
        try {
            redisTemplate.opsForValue().set(REDIS_TMP_TICKET + ":" + tmpTicket,
                    MD5Utils.getMD5Str(tmpTicket), 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpTicket;
    }

    @RequestMapping("/loginout")
    @ResponseBody
    @ApiOperation(value = "用户退出登录")
    public R loginOut(String userId,
                                    HttpServletRequest request,
                                    HttpServletResponse response){
        //获取cookie中的全局票据
        String userTicket = getCookie(COOKIE_USER_TICKET, request);
        //清除cookie和全局票据
        delCookie(COOKIE_USER_TICKET, response);
        redisTemplate.delete(REDIS_USER_TICKET + ":" + userTicket);
        //清除用户会话数据
        redisTemplate.delete(REDIS_USER_TOKEN + ":" + userId);
        return R.ok();
    }

    private void delCookie(String key, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, null);
        cookie.setDomain("ssp.com");
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }

}
