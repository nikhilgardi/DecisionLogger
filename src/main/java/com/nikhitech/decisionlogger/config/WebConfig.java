package com.nikhitech.decisionlogger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import com.nikhitech.decisionlogger.security.ActiveMenuInterceptor;
import com.nikhitech.decisionlogger.security.RBACInterceptor;

@Configuration
@EnableWebMvc
@ComponentScan("com.nikhitech.decisionlogger.controller")
public class WebConfig implements WebMvcConfigurer {

    private final RBACInterceptor rbacInterceptor;

    public WebConfig(RBACInterceptor rbacInterceptor) {
        this.rbacInterceptor = rbacInterceptor;
    }

    /*
    ==========================================================
    THYMELEAF TEMPLATE RESOLVER
    ==========================================================
    */
    @Bean
    public SpringResourceTemplateResolver templateResolver() {

        SpringResourceTemplateResolver resolver =
                new SpringResourceTemplateResolver();

        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);

        return resolver;
    }

    /*
    ==========================================================
    TEMPLATE ENGINE
    ==========================================================
    */
    @Bean
    public SpringTemplateEngine templateEngine() {

        SpringTemplateEngine engine =
                new SpringTemplateEngine();

        engine.setTemplateResolver(templateResolver());

        return engine;
    }

    /*
    ==========================================================
    VIEW RESOLVER
    ==========================================================
    */
    @Bean
    public ViewResolver viewResolver() {

        ThymeleafViewResolver resolver =
                new ThymeleafViewResolver();

        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");

        return resolver;
    }

    /*
    ==========================================================
    RBAC INTERCEPTOR
    ==========================================================
    */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    	/*
         * RBAC security interceptor
         */
    	
        registry.addInterceptor(rbacInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/**", "/static/**");
        
        /*
         * ACTIVE MENU INTERCEPTOR
         *
         * Handles sidebar highlighting
         */
        registry.addInterceptor(new ActiveMenuInterceptor())
                .addPathPatterns("/**");
    }

    /*
    ==========================================================
    STATIC RESOURCE HANDLER
    ==========================================================
    */

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/static/**")
                .addResourceLocations("/static/");
    }
    
    
}