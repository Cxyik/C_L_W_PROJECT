package com.example.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;

public interface WebMvcConfigurerAdapter {

    public void addCorsMappings(CorsRegistry registry) ;
}
