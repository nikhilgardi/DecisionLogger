package com.nikhitech.decisionlogger.config;

import com.nikhitech.decisionlogger.security.RBACInterceptor;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring5.*;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

/*
 * ===============================================================
 * WEB CONFIGURATION
 * ===============================================================
 *
 * LAYER:
 * - Presentation Layer Configuration
 *
 * PURPOSE:
 * - Configure MVC
 * - Configure Thymeleaf
 * - Register Interceptors
 *
 *
 * DESIGN PATTERNS:
 * ---------------------------------------------------------------
 * 1. MVC Pattern
 * 2. Strategy Pattern (ViewResolver)
 * 3. Interceptor Pattern
 */

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private final RBACInterceptor rbacInterceptor;

    /*
     * Constructor Injection
     *
     * WHY?
     * - Spring injects RBACInterceptor bean.
     * - No manual "new" keyword.
     */
    public WebConfig(RBACInterceptor rbacInterceptor) {
        this.rbacInterceptor = rbacInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(rbacInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/**", "/css/**", "/js/**");
    }
}