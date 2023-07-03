package com.batab.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableJpaAuditing
@SpringBootApplication
@EnableWebSocket
public class MyBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyBlogApplication.class, args);

    }
    @Bean(name = "uploadPath")
    public String uploadPath() {
        return "d:/image/";
    }



}
