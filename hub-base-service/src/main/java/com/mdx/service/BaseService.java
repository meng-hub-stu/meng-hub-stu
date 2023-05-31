package com.mdx.service;

import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Mengdl
 * @date 2023/05/31
 */

public interface BaseService<T> extends IService<T> {

    /**
     * 删除数据
     * @param ids 主键id
     * @return 返回结果
     */
    boolean deleteLogic(@NotEmpty List<Long> ids);

}
