package com.mdx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdx.mapper.UserMapper;
import com.mdx.pojo.User;
import com.mdx.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Mengdl
 * @date 2023/04/25
 */
@AllArgsConstructor
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
