package superapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import superapp.util.GeoLocationAPI.Coordinate;
import superapp.util.GeoLocationAPI.LiftGeoLocationHandler;
import superapp.util.GeoLocationAPI.LiftLanguage;
import superapp.util.GeoLocationAPI.MapBoxConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(Application.class);
		Properties defaultProperties = new Properties();

		String activeProfile = System.getenv("ACTIVE_PROFILE");
		if (activeProfile == null || activeProfile.isBlank())
			activeProfile = "staging";
		defaultProperties.setProperty("spring.profiles.active", activeProfile);
		defaultProperties.setProperty("spring.config.name", "application-${spring.profiles.active}");
		application.setDefaultProperties(defaultProperties);
		application.run(args);
		LiftGeoLocationHandler check = new LiftGeoLocationHandler(new MapBoxConverter());
		ArrayList<String> arr = new ArrayList<>();
		arr.add("Afeka Collage");
		arr.add("Sarona Market");
		HashMap<String,Object> rv = (HashMap<String, Object>) check.getDirectionsByAddress(LiftLanguage.EN,arr);

	}
}
