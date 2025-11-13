package com.ruralwomen.platform;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


@EnableScheduling
@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
        org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
        org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration.class
})
public class RuralwomenPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(RuralwomenPlatformApplication.class, args);
	}
    @Bean
    public CommandLineRunner printEndpoints(RequestMappingHandlerMapping mapping) {
        return args -> mapping.getHandlerMethods().forEach((k,v) -> System.out.println(k));
    }
}

