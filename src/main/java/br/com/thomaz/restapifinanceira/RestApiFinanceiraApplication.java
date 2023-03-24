package br.com.thomaz.restapifinanceira;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class RestApiFinanceiraApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiFinanceiraApplication.class, args);
	}
	
}
