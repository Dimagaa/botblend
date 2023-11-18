package com.app.botblend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BotblendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BotblendApplication.class, args);
    }

}
