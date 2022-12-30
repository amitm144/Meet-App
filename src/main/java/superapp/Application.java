package superapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(Application.class);
		Properties defaultProperties = new Properties();
		/*  INSTRUCTIONS:
		    add and or change ACTIVE_PROFILE environment variable to choose the correct properties.
		    the various property files will determine the application behaviour:
		    1. production - HelperInitializer will be DISABLED and JPA will connect to the PRODUCTION db
		    	changes you will make WILL NOT be deleted from the database when you shut down the server.
		    2. staging - HelperInitializer will be ENABLED and JPA will connect to the STAGING db
		    	this setting is appropriate for testing in similar environment to production
		    	changes you will make WILL be deleted from the database when you shut down the server.
		    3. test - HelperInitializer will be DISABLED and JPA will connect to H2,
		    	this setting is appropriate for unit testing
		    	or if you wish to play with the app locally with no effect on any database
		    BY DEFAULT THE APPLICATION WILL START WITH STAGING PROPERTIES APPLIED
		*/
		String activeProfile = System.getenv("ACTIVE_PROFILE");
		if (activeProfile == null || activeProfile.isBlank())
			activeProfile = "staging";
		defaultProperties.setProperty("spring.profiles.active", activeProfile);
		defaultProperties.setProperty("spring.config.name", "application-${spring.profiles.active}");
		application.setDefaultProperties(defaultProperties);
		application.run(args);
	}
}
