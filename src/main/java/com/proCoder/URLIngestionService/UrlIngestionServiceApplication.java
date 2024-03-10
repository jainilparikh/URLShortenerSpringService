package com.proCoder.URLIngestionService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.proCoder.*"})
public class UrlIngestionServiceApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext =
				 SpringApplication.run(UrlIngestionServiceApplication.class, args);
	}

}
