package com.mdx.login.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mdx.login.util.TokenUtil;
import com.mdx.login.vo.AuthInfo;
import com.mdx.pojo.User;
import com.mdx.pojo.bo.UserBO;
import com.mdx.service.IUserService;
import com.mdx.util.CookieUtils;
import com.mdx.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.mdx.login.constant.TokenConstant.REDIS_USER_TOKEN;
import static java.util.Objects.isNull;

/**
 * 用户登录
 * @author Mengdl
 * @date 2023/04/26
 */
@Slf4j
@RestController
@RequestMapping(value = "/auth")
@AllArgsConstructor
@Api(value = "登录", tags = "登录")
public class AuthController {

    private final IUserService userService;
    private final RedisTemplate<String, Object> redisTemplate;

    @PostMapping(value = "login")
    @ApiOperation(value = "用户登录", notes = "传入对象", httpMethod = "POST")
    public R<AuthInfo> login(@RequestBody UserBO userBO,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        //查询并生成token
        User loginUser = userService.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUserName, userBO.getUserName())
                .eq(User::getPassword, userBO.getPassword()));
        if (isNull(loginUser)) {
            return R.errorMsg("用户不存在");
        }
        //省略校验
        AuthInfo authInfo = TokenUtil.createAuthInfo(loginUser);
        //token放在redis中就可以了
        redisTemplate.opsForValue().set(REDIS_USER_TOKEN + ":" + authInfo.getId(), authInfo.getToken());
        //可以设置cookie
        CookieUtils.setCookie(request, response, "user", JSON.toJSONString(authInfo), true);
        return R.ok(authInfo);
    }

    @PostMapping(value = "logout")
    @ApiOperation(value = "推出登录", notes = "传入用户id", httpMethod = "POST")
    public R<Boolean> loginOut(@RequestParam(value = "userId") String userId,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        //清除redis中用户信息
        redisTemplate.delete(REDIS_USER_TOKEN + ":" + userId);
        //清空cookie
        CookieUtils.deleteCookie(request, response, "user");
        return R.ok();
    }

}
