package com.avast.server.libver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Collections;

@SpringBootApplication
@ComponentScan("com.avast.server.libver.service")
public class Application {

    @Bean
    public RestTemplate restTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(jacksonSupportsText());
        return restTemplate;
    }


    private HttpMessageConverter jacksonSupportsText() {//Gitlab returns JSON as plain text
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.parseMediaType("text/plain;charset=utf-8")));
        return converter;
    }

    public static void main(String[] args) {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "10");
        SpringApplication.run(Application.class, args);
    }

    public static String getImplementationVersion() {
        return Application.class.getPackage().getImplementationVersion();
    }


}
