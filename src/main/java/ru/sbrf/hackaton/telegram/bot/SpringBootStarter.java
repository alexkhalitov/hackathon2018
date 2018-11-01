package ru.sbrf.hackaton.telegram.bot;

import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class SpringBootStarter {
	
	public static void main(String[] args) {
		ApiContextInitializer.init(); // Инициализируем апи
		SpringApplication.run(SpringBootStarter.class, args);
		System.out.println("Spring Boot Starter");
	}

	@Bean
	public EmbeddedServletContainerFactory embeddedServletContainerFactory() {
		return new TomcatEmbeddedServletContainerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
			}
		};
	}

}
