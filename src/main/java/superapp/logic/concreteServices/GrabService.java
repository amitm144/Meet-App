package superapp.logic.concreteServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.converters.SuperAppObjectConverter;
import superapp.dal.SuperAppObjectEntityRepository;
import superapp.dal.UserEntityRepository;
import superapp.data.GrabCuisines;
import superapp.data.SuperAppObjectEntity;
import superapp.logic.ServicesFactory;
import superapp.util.exceptions.InvalidInputException;
import superapp.util.exceptions.NotFoundException;
import superapp.util.wrappers.SuperAppObjectIdWrapper;

import java.util.*;

@Service
public class GrabService implements ServicesFactory , superapp.logic.GrabService {
	private UserEntityRepository userEntityRepository;
	private SuperAppObjectEntityRepository objectRepository;
	private SuperAppObjectConverter converter;

	@Autowired
	public GrabService(SuperAppObjectEntityRepository objectRepository, UserEntityRepository userEntityRepository) {
		super();
		this.objectRepository = objectRepository;
		this.userEntityRepository = userEntityRepository;
		this.converter = new SuperAppObjectConverter();
	}

	@Override
	public void handleObjectByType(SuperAppObjectBoundary object) {
		// TODO: CHECK IF NECSSARY TO IMPLEMENT SERVICES FACTORY
	}

	@Override
	public void updateObjectDetails(SuperAppObjectEntity object) {
		// TODO: CHECK IF NECSSARY TO IMPLEMENT SERVICES FACTORY
	}

	@Override
	public Object runCommand(String miniapp,
	                         SuperAppObjectIdWrapper targetObject,
	                         UserIdBoundary invokedBy,
	                         String commandCase, Map<String, Object> commandAttributes) {
		SuperAppObjectEntity group =
				this.objectRepository.findById(
						(new SuperAppObjectEntity.SuperAppObjectId(
								targetObject.getObjectId().getSuperapp(),
								targetObject.getObjectId().getInternalObjectId())))
				.orElseThrow(() ->  new NotFoundException("group not found"));
		if (!checkUserInGroup(group, invokedBy))
			throw new InvalidInputException("Invoking user is not part of this group");

		SuperAppObjectEntity grab = (SuperAppObjectEntity)group.getChildren().stream().toList().get(0);


		switch(commandCase) {
			case "addCuisine": {
				this.addCuisine(grab,commandAttributes);
			}
			case "selectRandomCuisine": {
				return this.selectRandomCuisine(grab);
			}
			case "resetGrabGroup": {
				this.resetGrabGroup(grab);
			}

			default:
				throw new NotFoundException("Unknown command");
		}
	}

	private boolean checkUserInGroup(SuperAppObjectEntity group, UserIdBoundary userId) {
		LinkedHashMap<String, String> linkedMap = new LinkedHashMap<>();
		linkedMap.put("superapp", userId.getSuperapp());
		linkedMap.put("email", userId.getEmail());

		return ((List<LinkedHashMap<String, String>>)this.converter
				.detailsToMap(group.getObjectDetails()).get("members"))
				.contains(linkedMap);
	}


	@Override
	public void addCuisine(SuperAppObjectEntity grab, Map<String, Object> commandAttributes) {
		ArrayList<GrabCuisines> chosenCuisines = (ArrayList<GrabCuisines>)this.converter.detailsToMap(grab.getObjectDetails()).get("cuisines");
		GrabCuisines additionalCuisines = (GrabCuisines)this.converter.detailsToMap(grab.getObjectDetails()).get("cuisine");
		chosenCuisines.add(additionalCuisines);

		Map<String,Object> chosenCuisinesToSave = new HashMap<>();
		chosenCuisinesToSave.put("cuisines",chosenCuisines);
		grab.setObjectDetails(converter.detailsToString(chosenCuisinesToSave));

		//todo need to save new object's details
	}

	@Override
	public Object selectRandomCuisine(SuperAppObjectEntity grab) {
		ArrayList<GrabCuisines> chosenCuisines = (ArrayList<GrabCuisines>)this.converter.detailsToMap(grab.getObjectDetails()).get("cuisines");

		return chosenCuisines.get(new Random().nextInt(chosenCuisines.size()));

	}

	@Override
	public void resetGrabGroup(SuperAppObjectEntity grab) {
		grab.setActive(false);
	}
}
