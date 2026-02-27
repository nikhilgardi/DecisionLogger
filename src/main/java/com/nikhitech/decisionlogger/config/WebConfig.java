package com.nikhitech.decisionlogger.config;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring5.*;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.nikhitech.decisionlogger")
public class WebConfig {

 @Bean
 public SpringResourceTemplateResolver templateResolver() {
     SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
     resolver.setPrefix("/WEB-INF/views/");
     resolver.setSuffix(".html");
     resolver.setTemplateMode("HTML");
     resolver.setCharacterEncoding("UTF-8");
     resolver.setCacheable(false);
     return resolver;
 }

 @Bean
 public SpringTemplateEngine templateEngine() {
     SpringTemplateEngine engine = new SpringTemplateEngine();
     engine.setTemplateResolver(templateResolver());
     return engine;
 }

 @Bean
 public ViewResolver viewResolver() {
     ThymeleafViewResolver resolver = new ThymeleafViewResolver();
     resolver.setTemplateEngine(templateEngine());
     resolver.setCharacterEncoding("UTF-8");
     return resolver;
 }
}