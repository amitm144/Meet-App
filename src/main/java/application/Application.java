package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"controllers"}) // shows the application where the rest controllers are
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
