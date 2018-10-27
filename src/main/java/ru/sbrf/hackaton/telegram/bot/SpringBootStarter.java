package ru.sbrf.hackaton.telegram.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class SpringBootStarter {
	
	public static void main(String[] args) {
		ApiContextInitializer.init(); // Инициализируем апи
		SpringApplication.run(SpringBootStarter.class, args);
		System.out.println("Spring Boot Starter");
	}

}
