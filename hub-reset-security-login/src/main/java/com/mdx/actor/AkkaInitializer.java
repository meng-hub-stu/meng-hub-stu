package com.mdx.actor;

import akka.actor.ActorSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * akka是一种基于Scala的网络编程库，实现了RPC框架
 * Akka Init
 * @author huan
 */
@Component
public class AkkaInitializer implements ServletContextListener {

    private static Logger logger = LoggerFactory.getLogger("AkkaInitializer");

    @Autowired
    private ActorSystem actorSystem;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("---------------Start ActorSystem---------------");
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (actorSystem != null) {
            logger.info("---------------Killing ActorSystem---------------");
            actorSystem.terminate();
        } else {
            logger.warn("---------------No ActorSystem---------------");
        }
    }
}
