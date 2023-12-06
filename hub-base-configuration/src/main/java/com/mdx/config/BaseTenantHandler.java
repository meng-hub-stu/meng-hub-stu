package com.mdx.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;

/**
 * @author Mengdl
 * @date 2023/05/31
 */
@AllArgsConstructor
public class BaseTenantHandler implements TenantLineHandler {
    //可在其中获取配置
    private final BladeTenantProperties properties;

    @Override
    public Expression getTenantId() {
        return new StringValue("000000");
    }

    @Override
    public String getTenantIdColumn() {
        return properties.getColumn();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return !(
                (properties.getTables().size() > 0 && properties.getTables().contains(tableName))
                        || properties.getBladeTables().contains(tableName)
                );
    }

}
