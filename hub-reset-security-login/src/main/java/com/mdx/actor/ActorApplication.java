package com.mdx.actor;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * akka启动配置系统
 * @author austinChen
 * Created by Austin on 2016/3/22.
 */
@Component
@Slf4j
public class ActorApplication {

    @Autowired
    private ApplicationContext applicationContext;
    public @Bean
    ActorSystem actorSystem() {
        try {

            String akkaPath="akka.conf";
            //创建akka系统
            log.info("加载akka配置,路径:{}",akkaPath);
            Config config = ConfigFactory.load(akkaPath);
            log.info("ActorSystem 初始化...");
            log.info("akka.default-dispatcher 初始化参数 parallelism-min:{}",config.getInt("akka.actor.default-dispatcher.fork-join-executor.parallelism-min"));
            log.info("akka.default-dispatcher 初始化参数 parallelism-factor:{}",config.getInt("akka.actor.default-dispatcher.fork-join-executor.parallelism-factor"));
            log.info("akka.default-dispatcher 初始化参数 parallelism-max:{}",config.getInt("akka.actor.default-dispatcher.fork-join-executor.parallelism-max"));
            //创建akka系统
            ActorSystem actorSystem = ActorSystem.create("sys",config );
            log.info("ActorSystem 初始化... Name="+actorSystem.name());
            //每个ActorSystem都会持有一个ApplicationContext
            SpringExtension.SPRING_EXTENSION.get(actorSystem).initialize(applicationContext);
            return actorSystem;
        } catch (Exception e) {
            log.error("ActorSystem 启动失败：", e);
            return null;
        }
    }
}
