package com.mdx.config.datasource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author Mengdl
 * @date 2023/04/27
 */
@Configuration
@EnableTransactionManagement
public class EntityManagerConf {
    @Resource
    private DataSource defaultDataSource;

   /* @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager tm =
                new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory);
        tm.setDataSource(defaultDataSource);
        return tm;
    }*/
}
