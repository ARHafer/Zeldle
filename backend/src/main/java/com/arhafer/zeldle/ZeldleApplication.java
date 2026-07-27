package com.arhafer.zeldle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZeldleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZeldleApplication.class, args);
	}

}
