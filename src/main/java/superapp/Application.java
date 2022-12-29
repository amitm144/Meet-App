package superapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(Application.class);
		Properties defaultProperties = new Properties();

		/* TODO: activate on production environment */
		defaultProperties.setProperty("spring.profiles.active", "production");
		/* TODO: activate on stageing environment (if you want to test against MySql test db) */
//		defaultProperties.setProperty("spring.profiles.active", "staging");
		/* TODO: activate on production environment (if you want to test with h2 locally) */
//		defaultProperties.setProperty("spring.profiles.active", "test");

		defaultProperties.setProperty("spring.config.name", "application-${spring.profiles.active}");
		application.setDefaultProperties(defaultProperties);
		application.run(args);
	}
}
