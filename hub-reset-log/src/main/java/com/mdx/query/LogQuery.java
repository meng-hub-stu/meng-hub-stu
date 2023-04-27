package com.mdx.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdx.util.BasePageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.lang.StringUtils;

/**
 * @author Mengdl
 * @date 2023/04/26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LogQuery extends BasePageQuery {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @Override
    public <T> QueryWrapper<T> wrapperQuery() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(id.toString()), "id", id);
        return queryWrapper;
    }


}
