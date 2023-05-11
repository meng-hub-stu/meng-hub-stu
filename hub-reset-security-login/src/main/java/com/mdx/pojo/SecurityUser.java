package com.mdx.pojo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author ：YangYaNan
 * @date ：Created in 9:46 2022/6/29
 * @description ：
 * @version: 1.0
 */
public class SecurityUser implements UserDetails, Serializable {

    private User user;

    private String jwt;

    private String jti;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authList = new ArrayList<>();
        for(Role role:this.user.getRoles()){
            //1.1角色关键词
           /* authList.add(new SimpleGrantedAuthority(role.getKeyword()));
            for (Permission permission:role.getPermissions()){
                //1.2权限关键词
                authList.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }*/
        }
        return authList;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return ObjectUtils.isEmpty(this.user) ? "" :this.user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
//        return UserStatusEnum.EFFECTIVE.getCode().equals(this.user.getState());
        return true;
    }

    public SecurityUser(User user, String jwt, String jti) {
        this.user = user;
        this.jwt = jwt;
        this.jti = jti;
    }

    public SecurityUser(User user, String jti) {
        this.user = user;
        this.jti = jti;
    }

    public SecurityUser() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
