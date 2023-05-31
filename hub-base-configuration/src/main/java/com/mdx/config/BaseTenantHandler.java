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
        return "tenant_id";
    }

    @Override
    public boolean ignoreTable(String tableName) {
        //测试使用
        if (tableName.equals("hub_dept")) {
            return false;
        }
        return true;
    }

}
