package com.assesment.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@SpringBootApplication
@EnableAsync
public class EventNotifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventNotifierApplication.class, args);
	}

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public Random random(){
        return new Random();
    }

}
