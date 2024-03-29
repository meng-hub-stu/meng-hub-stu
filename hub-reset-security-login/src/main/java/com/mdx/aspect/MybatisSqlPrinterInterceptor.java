package com.mdx.aspect;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;

/**
 * mybatis打印sql拦截器
 * @author austinChen
 * Created by austinChen on 2019/4/23 10:59.
 */
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {
        MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query", args = {
        MappedStatement.class, Object.class, RowBounds.class,
        ResultHandler.class}),
    @Signature(type = Executor.class, method = "query", args = {
    MappedStatement.class, Object.class, RowBounds.class,
    ResultHandler.class, CacheKey.class, BoundSql.class})})
@SuppressWarnings({"unchecked", "rawtypes"})
@Component
public class MybatisSqlPrinterInterceptor implements Interceptor {

    private static final Logger log = LoggerFactory.getLogger(MybatisSqlPrinterInterceptor.class);

    public MybatisSqlPrinterInterceptor() {
        log.info("MybatisSqlPrinterInterceptor");
    }

    //允许无条件查询的表名集合
    private List<String> allowFullTable= Arrays.asList(new String[]{"general_pay_platform","renewal_meta","role","error_category","role_permission_meta","protocol_user_channel_config"});

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            // 获取xml中的一个select/update/insert/delete节点，主要描述的是一条SQL语句
            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
            Object parameter = null;
            // 获取参数，if语句成立，表示sql语句有参数，参数格式是map形式
            if (invocation.getArgs().length > 1) {
                parameter = invocation.getArgs()[1];
            }
            // 获取到节点的id,即sql语句的id
            String sqlId = mappedStatement.getId();

           // (BoundSql) invocation.getArgs()[5]
            // BoundSql就是封装myBatis最终产生的sql类

            BoundSql boundSql = mappedStatement.getBoundSql(parameter);
            if(sqlId.contains("RowBounds"))
            {
                boundSql=(BoundSql)invocation.getArgs()[5];
            }
            // 获取节点的配置
            Configuration configuration = mappedStatement.getConfiguration();
            // 获取到最终的sql语句
            String sql = getSql(configuration, boundSql, sqlId);
            log.info("sql = {}", sql);
            boolean isAllow=false;
            if(sql.contains("where")||sql.contains("WHERE")||sql.contains("insert")||sql.contains("INSERT")||sql.contains("AND ")||sql.contains("and "))
            {
                isAllow=true;
            }
            if(!isAllow) {
                //允许无条件查询的表名集合
                for (String tableName : allowFullTable) {
                    if (sql.contains(tableName)) {
                        isAllow = true;
                        break;
                    }
                }
            }
            if(!isAllow) {
                //throw new BusException("不在允许无条件查询的表中！");
            }
        } catch (Exception e) {
            log.error("sql拦截器执行出错！",e);
        }
        // 执行完上面的任务后，不改变原有的sql执行过程
        return invocation.proceed();
    }

    /**
     * @param configuration  配置
     * @param boundSql 绑定的sql
     * @param sqlId sqlid
     * @return 封装了一下sql语句，使得结果返回完整xml路径下的sql语句节点id + sql语句
     */
    public static String getSql(Configuration configuration, BoundSql boundSql, String sqlId) {
        String sql = showSql(configuration, boundSql);
        StringBuilder str = new StringBuilder(100);
        str.append(sqlId);
        str.append(" : ");
        str.append(sql);
        return str.toString();
    }

    /**
     * @param obj 待处理的参数对象
     * @return 如果参数是String，则添加单引号， 如果是日期，则转换为时间格式器并加单引号； 对参数是null和不是null的情况作了处理
     */
    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            if(obj!=null) {
                value = "'" + formatter.format((Date)obj) + "'";
            }
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }

        }
        return value;
    }

    /**
     * @param configuration 配置
     * @param boundSql boundSql对象
     * @return 进行？的替换 返回真实的sql语句
     */
    public static String showSql(Configuration configuration, BoundSql boundSql) {
        // 获取参数
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql
            .getParameterMappings();
        // sql语句中多个空格都用一个空格代替
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (CollectionUtils.isNotEmpty(parameterMappings) && parameterObject != null) {
            // 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换<br>　　　　　　　// 如果根据parameterObject.getClass(）可以找到对应的类型，则替换
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));

            } else {
                // MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,主要支持对JavaBean、Collection、Map三种类型对象的操作
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        // 该分支是动态sql
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else {
                        //打印出缺失，提醒该参数缺失并防止错位
                        sql = sql.replaceFirst("\\?", "缺失");
                    }
                }
            }
        }
        return sql;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        //不做任何事
    }
}
