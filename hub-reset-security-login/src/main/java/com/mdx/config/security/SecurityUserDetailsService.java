package com.mdx.config.security;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mdx.pojo.User;
import com.mdx.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * @author Mengdl
 * @date 2023/05/10
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUserName, userName));
        checkUserInfo(user);
        return null;
    }

    private void checkUserInfo(User user) {
        if(ObjectUtils.isEmpty(user)){
            log.info("登录失败,查询不到用户信息");
            throw new UsernameNotFoundException("账户不存在");
        }
        if(ObjectUtils.isEmpty(user.getRoles())){
            log.info("登录失败,未绑定角色，不允许登录！");
            throw new BadCredentialsException("用户未绑定角色,不允许登录,请联系管理员！");
        }
    }

}
