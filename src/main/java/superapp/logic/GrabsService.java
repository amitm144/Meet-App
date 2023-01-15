package superapp.logic;

import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.data.GrabCuisines;
import superapp.data.SuperAppObjectEntity;

import java.util.List;
import java.util.Map;

public interface GrabsService {
	public Map<GrabCuisines, Integer>  addVote(SuperAppObjectEntity poll, List<GrabCuisines> votes);
	public Object selectRandomly(SuperAppObjectEntity poll);
	public SuperAppObjectBoundary selectByMajority(SuperAppObjectEntity poll);

}
