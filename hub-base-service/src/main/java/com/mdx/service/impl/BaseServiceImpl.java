package com.mdx.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdx.pojo.BaseEntity;
import com.mdx.pojo.BaseUser;
import com.mdx.service.BaseService;
import com.mdx.util.WebUtils;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * @author Mengdl
 * @date 2023/05/31
 */
@Validated
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {

    @Override
    public boolean save(T entity) {
        //在token中获取登录人得信息即可
        HttpServletRequest request = WebUtils.getRequest();
        BaseUser user = new BaseUser();
        user.setUserId(1L);
        if (user != null) {
            entity.setCreateUser(user.getUserId());
            entity.setUpdateUser(user.getUserId());
        }
        Date now = DateUtil.date();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        if (entity.getStatus() == null) {
            entity.setStatus(1);
        }
        entity.setIsDeleted(0);
        return super.save(entity);
    }

    @Override
    public boolean updateById(T entity) {
        HttpServletRequest request = WebUtils.getRequest();
        BaseUser user = new BaseUser();
        user.setUserId(1L);
        if (user != null) {
            entity.setUpdateUser(user.getUserId());
        }

        entity.setUpdateTime(DateUtil.date());
        return super.updateById(entity);
    }

    @Override
    public boolean deleteLogic(@NotEmpty List<Long> ids) {
        return super.removeByIds(ids);
    }

}
