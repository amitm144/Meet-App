package superapp.logic.concreteServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.converters.SuperAppObjectConverter;
import superapp.dal.SuperAppObjectEntityRepository;
import superapp.dal.UserEntityRepository;
import superapp.data.SuperAppObjectEntity;
import superapp.logic.ServicesFactory;
import superapp.logic.SplitsService;
import superapp.util.exceptions.InvalidInputException;
import superapp.util.exceptions.NotFoundException;
import superapp.util.wrappers.SuperAppObjectIdWrapper;

import java.util.*;

@Service
public class SplitService implements SplitsService, ServicesFactory {
	private UserEntityRepository userEntityRepository;
	private SuperAppObjectEntityRepository objectRepository;
	private SuperAppObjectConverter converter;

	@Autowired
	public SplitService(SuperAppObjectEntityRepository objectRepository, UserEntityRepository userEntityRepository) {
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
		switch(commandCase) {
			case "showDebt": {
				return this.showDebt(group, invokedBy);
			}
			case "showAllDebts": {
				return this.showAllDebts(group);
			}
			case "settleGroupDebts": {
				this.settleGroupDebts(group);
				return null;
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
	public float showDebt(SuperAppObjectEntity group, UserIdBoundary user) {
		Set<SuperAppObjectEntity> transactions = group.getChildren();
		List<UserIdBoundary> users = (List<UserIdBoundary>)this.converter.detailsToMap(group.getObjectDetails()).get("members");
		int totalUsers = users.size();
		float totalPayments = 0, userPayments = 0;
		for (SuperAppObjectEntity t : transactions) {
			if (!t.getActive()) // check if transaction has alredy been settled
				continue;

			float amount = Double.valueOf((double)this.converter.detailsToMap(t.getObjectDetails()).get("amount")).floatValue();
			if (t.getCreatedBy().getUserId().equals(user))
				userPayments += amount;
			totalPayments += amount;
		}
		float debt = userPayments - (totalPayments / totalUsers);
		return debt > 0 ? 0 : debt;
	}

	@Override
	public Object showAllDebts(SuperAppObjectEntity group) {
		Map<UserIdBoundary, Float> allDebt = new HashMap<>();
		((List<LinkedHashMap<String, String>>)this.converter
				.detailsToMap(group.getObjectDetails()).get("members"))
				.forEach(userData -> {
					UserIdBoundary user = new UserIdBoundary(userData.get("superapp"), userData.get("email"));
					allDebt.put(user, showDebt(group, user));
				});
		return allDebt;
	}

	@Override
	public void settleGroupDebts(SuperAppObjectEntity group) {
		group.getChildren().forEach(t -> t.setActive(false));
	}
}
