package br.com.flagrace.bot.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"br.com.flagrace.bot"})
@EntityScan(basePackages  = "br.com.flagrace.bot")
@EnableJpaRepositories(basePackages = "br.com.flagrace.bot")
public class FlagRaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlagRaceApplication.class, args);
	}

}
