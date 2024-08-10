package com.eCommerc;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EnableJpaRepositories("com.eCommerc.model.persistence.repositories")
@EntityScan("com.eCommerc.model.persistence")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class eCommercApplication {

	private static final Logger log = LogManager.getLogger(eCommercApplication.class);
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
	public static void main(String[] args) {
		SpringApplication.run(eCommercApplication.class, args);
		log.info("Ecommerce Application Started");
	}
}

