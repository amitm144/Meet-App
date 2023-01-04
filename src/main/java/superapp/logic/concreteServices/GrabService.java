package superapp.logic.concreteServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import superapp.boundaries.grab.GrabPollBoundary;
import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.converters.SuperAppObjectConverter;
import superapp.dal.SuperAppObjectEntityRepository;
import superapp.data.GrabCuisines;
import superapp.data.ObjectTypes;
import superapp.data.SuperAppObjectEntity;
import superapp.data.SuperappObjectPK;
import superapp.logic.GrabsService;
import superapp.logic.MiniAppServiceHandler;
import superapp.util.exceptions.CannotProcessException;
import superapp.util.exceptions.InvalidInputException;
import superapp.util.exceptions.NotFoundException;
import superapp.util.wrappers.SuperAppObjectIdWrapper;

import java.util.*;

import static superapp.data.ObjectTypes.GrabPoll;
import static superapp.data.ObjectTypes.isValidObjectType;

@Service
public class GrabService implements GrabsService, MiniAppServiceHandler {
	private SuperAppObjectEntityRepository objectRepository;
	private SuperAppObjectConverter converter;

	private final Random RANDOM = new Random();
	private final List<GrabCuisines> CUISINES = Collections.unmodifiableList(Arrays.asList(GrabCuisines.values()));
	private final int CUISINES_SIZE = CUISINES.size();

	@Autowired
	public GrabService(SuperAppObjectEntityRepository objectRepository) {
		this.objectRepository = objectRepository;
		this.converter = new SuperAppObjectConverter();
	}

	@Override
	public void handleObjectByType(SuperAppObjectBoundary object) {
		String objectType = object.getType();
		if (!isValidObjectType(objectType) || ObjectTypes.valueOf(objectType) != GrabPoll)
			throw new InvalidInputException("Unknown object type");

		this.checkPollData(object);
	}

	@Override
	public Object runCommand(String miniapp, SuperAppObjectIdWrapper targetObject,
	                         UserIdBoundary invokedBy, String commandCase) {
		SuperAppObjectEntity group =
				this.objectRepository.findById(
						(new SuperappObjectPK(
								targetObject.getObjectId().getSuperapp(),
								targetObject.getObjectId().getInternalObjectId())))
				.orElseThrow(() ->  new NotFoundException("group not found"));

		if (!isUserInGroup(group, invokedBy))
			throw new InvalidInputException("Invoking user is not part of this group");

		SuperAppObjectEntity grab = (SuperAppObjectEntity)group.getChildren().stream().toList().get(0);

		switch(commandCase) {
			case "addCuisine": { this.addCuisine(grab); }
			case "selectRandomCuisine": { return this.selectRandomCuisine(grab); }
			case "disableGrabPoll": { return this.disableGrabPoll(grab); }
			default: throw new NotFoundException("Unknown command");
		}
	}

	@Override
	public void addCuisine(SuperAppObjectEntity poll) {
//		ArrayList<GrabCuisines> chosenCuisines =
//				(ArrayList<GrabCuisines>)this.converter.detailsToMap(poll.getObjectDetails()).get("cuisines");
//		GrabCuisines additionalCuisines = (GrabCuisines)this.converter.detailsToMap(poll.getObjectDetails()).get("cuisine");
//		chosenCuisines.add(additionalCuisines);
//
//		Map<String,Object> chosenCuisinesToSave = new HashMap<>();
//		chosenCuisinesToSave.put("cuisines",chosenCuisines);
//		grab.setObjectDetails(converter.detailsToString(chosenCuisinesToSave));

		// TODO
	}

	@Override
	public Object selectRandomCuisine(SuperAppObjectEntity poll) {
		GrabCuisines chosenCuisine = CUISINES.get(RANDOM.nextInt(CUISINES_SIZE));
		return new GrabPollBoundary(new SuperAppObjectIdBoundary(poll.getSuperapp(), poll.getObjectId()), chosenCuisine);
	}

	@Override
	public SuperAppObjectBoundary disableGrabPoll(SuperAppObjectEntity poll) {
		poll.setActive(false);
		this.objectRepository.save(poll);
		return this.converter.toBoundary(poll);
	}

	private void checkPollData(SuperAppObjectBoundary poll) {
		// TODO
		throw new CannotProcessException("METHOD NOT IMPLEMENTED");
	}

	private boolean isUserInGroup(SuperAppObjectEntity group, UserIdBoundary userId) {
		LinkedHashMap<String, String> linkedMap = new LinkedHashMap<>();
		linkedMap.put("superapp", userId.getSuperapp());
		linkedMap.put("email", userId.getEmail());

		return ((List<LinkedHashMap<String, String>>)this.converter
				.detailsToMap(group.getObjectDetails()).get("members"))
				.contains(linkedMap);
	}
}
