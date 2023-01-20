package com.my.project.controller.listeners;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.log4j.Log4j2;

import static com.my.project.constants.PageConstants.*;
import static com.my.project.constants.AttributeConstants.*;


/**
 * ServletContextListener implementation
 */
@Log4j2
@WebListener
public class AppContextListener implements ServletContextListener {
    /**
     * Initializes controller path relatively this particular application
     *
     * @param sce {@link ServletContextEvent} passed by application
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.setAttribute(CONTROLLER_ATTR, String.format("%s%s", context.getContextPath(), CONTROLLER_MAPPING));
        log.debug("Controller path in applicationContext set successful!");
    }

    /**
     * Removes previously created context attributes
     *
     * @param sce {@link ServletContextEvent} passed by application
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute(CONTROLLER_ATTR);
    }

}
