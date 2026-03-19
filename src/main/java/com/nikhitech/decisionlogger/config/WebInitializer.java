package com.nikhitech.decisionlogger.config;

import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebInitializer
        extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { RootConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    /*
     ===============================================================
     🔥 REAL FIX: Override DispatcherServlet
     ===============================================================
    */
    @Override
    protected DispatcherServlet createDispatcherServlet(
            org.springframework.web.context.WebApplicationContext servletAppContext) {

        DispatcherServlet dispatcherServlet =
                new DispatcherServlet(servletAppContext);

        // ✔ THIS is what actually works
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);

        return dispatcherServlet;
    }
    
    
    
    
}