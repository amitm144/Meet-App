package superapp.logic;

import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.data.SuperAppObjectEntity;

import java.util.Map;

public interface GrabsService {
	public void addCuisine(SuperAppObjectEntity poll);
	public Object selectRandomCuisine(SuperAppObjectEntity poll);
	public SuperAppObjectBoundary disableGrabPoll(SuperAppObjectEntity poll);

}
