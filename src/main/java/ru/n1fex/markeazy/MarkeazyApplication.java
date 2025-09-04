package ru.n1fex.markeazy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import ru.n1fex.markeazy.service.ProductService;

@SpringBootApplication
public class MarkeazyApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(MarkeazyApplication.class, args);

		ProductService productService = context.getBean(ProductService.class);
	}

}
