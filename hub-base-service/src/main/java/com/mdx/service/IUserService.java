package com.mdx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mdx.pojo.User;

/**
 * @author Mengdl
 * @date 2023/04/25
 */
public interface IUserService extends IService<User> {

    /**
     * 测试异步程序
     */
    void asyncData();

}
