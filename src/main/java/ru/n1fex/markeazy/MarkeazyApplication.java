package ru.n1fex.markeazy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MarkeazyApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(MarkeazyApplication.class, args);

	}

}
