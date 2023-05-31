package com.mdx.service.impl;

import com.mdx.mapper.DeptMapper;
import com.mdx.pojo.Dept;
import com.mdx.service.IDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Mengdl
 * @date 2023/05/31
 */
@Slf4j
@Service
public class DeptServiceImpl extends BaseServiceImpl<DeptMapper, Dept> implements IDeptService {

}
