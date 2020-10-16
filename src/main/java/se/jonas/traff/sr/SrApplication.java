package se.jonas.traff.sr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@EnableAutoConfiguration
@PropertySource("classpath:application.yml")
@ComponentScan(basePackages = {"se.jonas.traff"})
@ConfigurationPropertiesScan(basePackages = {"se.jonas.traff"})
@SpringBootApplication
public class SrApplication {

	public static void main(String[] args) {
		SpringApplication.run(SrApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
