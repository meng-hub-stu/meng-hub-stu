package com.mdx.login.vo;

import com.mdx.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取登录对象信息
 * @author Mengdl
 * @date 2023/04/26
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BladeUser extends User {
    private String sub;
}
