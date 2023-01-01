package superapp.logic;

import superapp.data.SuperAppObjectEntity;

import java.util.Map;

public interface GrabService {

	public void addCuisine(SuperAppObjectEntity grab, Map<String, Object> commandAttributes);

	public Object selectRandomCuisine(SuperAppObjectEntity group);
	public void resetGrabGroup(SuperAppObjectEntity group);

}
