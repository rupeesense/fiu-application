package com.rupeesense.fi;

import java.util.Set;
import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Slf4j
@SpringBootApplication
public class FIUServiceApplication {

	public static void main(String[] args) {
		System.setProperty("user.timezone", "UTC");
		SpringApplication.run(FIUServiceApplication.class, args);
	}

}
