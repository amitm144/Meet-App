package superapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import superapp.util.GeoLocationAPI.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
		MapBoxConverter mapBoxConverter = new MapBoxConverter();
		GrabGeoLocationHandler grab = new GrabGeoLocationHandler(mapBoxConverter);
		ArrayList<Map<String, Object>> rv;
		rv = grab.getResturantbyCuasie(GrabCuasine.HUMMUS, 1, 1);

//		LiftGeoLocationHandler check = new LiftGeoLocationHandler(new MapBoxConverter());
//		ArrayList<String> arr = new ArrayList<>();
//		arr.add("New York");
//		arr.add("Sarona Market");
//		rv = (HashMap<String, Object>) check.getDirectionsByAddress(LiftLanguage.EN, arr);
		rv.clear();

	}
}
