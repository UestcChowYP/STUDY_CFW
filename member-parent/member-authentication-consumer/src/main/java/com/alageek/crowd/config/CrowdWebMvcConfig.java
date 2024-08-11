package com.alageek.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/member/auth/to/reg/page").setViewName("member-reg");
        registry.addViewController("/member/auth/to/login/page").setViewName("member-login");
        registry.addViewController("/member/auth/to/center/page").setViewName("member-center");
        registry.addViewController("/member/auth/to/crowd/page").setViewName("member-crowd");
    }
}
