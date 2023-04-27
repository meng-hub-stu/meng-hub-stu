package com.mdx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdx.mapper.LogMapper;
import com.mdx.pojo.Log;
import com.mdx.service.ILogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Mengdl
 * @date 2023/04/26
 */
@Service
@AllArgsConstructor
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {
}
