package ru.practicum.myblog.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan("ru.practicum.myblog")
@RequiredArgsConstructor
public class WebConfig {
    @Bean
    MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
