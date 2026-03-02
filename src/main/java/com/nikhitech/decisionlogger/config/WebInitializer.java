package com.nikhitech.decisionlogger.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/*
 * ===============================================================
 * WEB INITIALIZER
 * ===============================================================
 *
 * DESIGN PATTERN:
 * - Front Controller Pattern
 *
 * WHAT IT DOES:
 * - Creates DispatcherServlet
 * - Loads RootConfig
 * - Loads WebConfig
 */

public class WebInitializer
        extends AbstractAnnotationConfigDispatcherServletInitializer {

    /*
     * Root Context:
     * - DataSource
     * - Services
     * - DAO
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { RootConfig.class };
    }

    /*
     * Web Context:
     * - Controllers
     * - ViewResolver
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebConfig.class };
    }

    /*
     * Map DispatcherServlet to "/"
     */
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}