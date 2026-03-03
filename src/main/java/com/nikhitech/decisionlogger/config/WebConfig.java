package com.nikhitech.decisionlogger.config;

import com.nikhitech.decisionlogger.security.RBACInterceptor;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

@Configuration
@EnableWebMvc
@ComponentScan("com.nikhitech.decisionlogger.controller")
public class WebConfig implements WebMvcConfigurer {

    private final RBACInterceptor rbacInterceptor;

    public WebConfig(RBACInterceptor rbacInterceptor) {
        this.rbacInterceptor = rbacInterceptor;
    }

    // 🔥 Thymeleaf Template Resolver
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

    // 🔥 Template Engine
    @Bean
    public SpringTemplateEngine templateEngine() {

        SpringTemplateEngine engine =
                new SpringTemplateEngine();

        engine.setTemplateResolver(templateResolver());

        return engine;
    }

    // 🔥 View Resolver
    @Bean
    public ViewResolver viewResolver() {

        ThymeleafViewResolver resolver =
                new ThymeleafViewResolver();

        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");

        return resolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rbacInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/**", "/css/**", "/js/**");
    }
}