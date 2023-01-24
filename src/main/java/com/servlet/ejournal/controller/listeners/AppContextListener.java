package com.servlet.ejournal.controller.listeners;

import com.servlet.ejournal.context.ApplicationContext;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.log4j.Log4j2;

import static com.servlet.ejournal.constants.PageConstants.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;


/**
 * ServletContextListener implementation
 */
@Log4j2
@WebListener
public class AppContextListener implements ServletContextListener {
    /**
     * Initializes controller path relatively this particular application
     * Initializes ApplicationContext class for use in application
     *
     * @param sce {@link ServletContextEvent} passed by application
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        context.setAttribute(CONTROLLER_ATTR, String.format("%s%s", context.getContextPath(), CONTROLLER_MAPPING));
        log.info("Controller path mapping set successful!");

        context.setAttribute(APPLICATION_CONTEXT, ApplicationContext.getInstance());
        log.info("Application context set successful!");
    }

    /**
     * Removes previously created context attributes
     *
     * @param sce {@link ServletContextEvent} passed by application
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute(CONTROLLER_ATTR);
        log.info("Controller path in applicationContext deleted!");

        sce.getServletContext().removeAttribute(APPLICATION_CONTEXT);
        log.info("Application context destroyed!");
    }

}
