package superapp.logic;

import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.data.GrabCuisines;
import superapp.data.SuperAppObjectEntity;

import java.util.Map;

public interface GrabsService {
	public void addVote(SuperAppObjectEntity poll, GrabCuisines[] votes);
	public Object selectRandomly(SuperAppObjectEntity poll);
	public SuperAppObjectBoundary selectByMajority(SuperAppObjectEntity poll);

}
