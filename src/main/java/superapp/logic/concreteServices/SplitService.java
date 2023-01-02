package superapp.logic.concreteServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.split.SplitDebtBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.converters.SuperAppObjectConverter;
import superapp.dal.SuperAppObjectEntityRepository;
import superapp.data.SuperAppObjectEntity;
import superapp.logic.MiniAppServiceHandler;
import superapp.logic.SplitsService;
import superapp.util.exceptions.CannotProcessException;
import superapp.util.exceptions.InvalidInputException;
import superapp.util.exceptions.NotFoundException;
import superapp.util.wrappers.SuperAppObjectIdWrapper;

import java.util.*;

import static superapp.data.ObjectTypes.isValidObjectType;

@Service
public class SplitService implements SplitsService, MiniAppServiceHandler {
	private SuperAppObjectEntityRepository objectRepository;
	private SuperAppObjectConverter converter;

	@Autowired
	public SplitService(SuperAppObjectEntityRepository objectRepository) {
		this.objectRepository = objectRepository;
		this.converter = new SuperAppObjectConverter();
	}

	@Override
	public void handleObjectByType(SuperAppObjectBoundary object) {
		String objectType = object.getType();
		if (!isValidObjectType(objectType))
			objectType = "";
		switch (objectType) {
			case ("GROUP") -> this.checkGroupData(object);
			case ("TRANSACTION") -> this.checkTransactionData(object);
			default -> throw new InvalidInputException("Unknown object type");
		}
	}

	@Override
	public Object runCommand(String miniapp, SuperAppObjectIdWrapper targetObject, UserIdBoundary invokedBy, String commandCase) {
		SuperAppObjectEntity group =
				this.objectRepository.findById(
						new SuperAppObjectEntity.SuperAppObjectId(
								targetObject.getObjectId().getSuperapp(),
								targetObject.getObjectId().getInternalObjectId()))
				.orElseThrow(() ->  new NotFoundException("group not found"));

		if (!isUserInGroup(group, invokedBy))
			throw new InvalidInputException("Invoking user is not part of this group");

		switch(commandCase) {
			case "showDebt": {
				SuperAppObjectEntity.SuperAppObjectId objectId =
						new SuperAppObjectEntity.SuperAppObjectId(group.getSuperapp(), group.getObjectId());
				return this.showDebt(objectId, invokedBy);
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

	@Override
	public SplitDebtBoundary showDebt(SuperAppObjectEntity.SuperAppObjectId groupId, UserIdBoundary user) {
		Optional<SuperAppObjectEntity> groupO = this.objectRepository.findById(groupId);
		if (groupO.isEmpty())
			return null;

		SuperAppObjectEntity group = groupO.get();
		Set<SuperAppObjectEntity> transactions = group.getChildren();
		int totalMembers =
				((List<UserIdBoundary>)this.converter.detailsToMap(group.getObjectDetails())
				.get("members"))
				.size();
		if (totalMembers == 0)
			throw new CannotProcessException("Group has no members");

		float totalPayments = 0, membersPayments = 0;
		for (SuperAppObjectEntity t : transactions) {
			if (!t.getActive()) // check if transaction has already been settled
				continue;

			float amount = doubleToFloat(this.converter.detailsToMap(t.getObjectDetails()).get("amount").toString());
			if (t.getCreatedBy().getUserId().equals(user))
				membersPayments += amount;
			totalPayments += amount;
		}

		float debt = membersPayments - (totalPayments / totalMembers);
		return new SplitDebtBoundary(user, debt > 0 ? 0 : debt);
	}

	@Override
	public Object showAllDebts(SuperAppObjectEntity group) {
		List<SplitDebtBoundary> allDebts = new ArrayList<>();
		((List<LinkedHashMap<String, String>>)this.converter
				.detailsToMap(group.getObjectDetails()).get("members"))
				.forEach(userData -> {
					UserIdBoundary userId = new UserIdBoundary(userData.get("superapp"), userData.get("email"));
					SuperAppObjectEntity.SuperAppObjectId objectId =
							new SuperAppObjectEntity.SuperAppObjectId(group.getSuperapp(), group.getObjectId());
					allDebts.add(showDebt(objectId, userId));
				});
		return allDebts.toArray(new SplitDebtBoundary[0]);
	}

	@Override
	public void settleGroupDebts(SuperAppObjectEntity group) {
		group.getChildren().forEach(t -> t.setActive(false));
	}

	private boolean isUserInGroup(SuperAppObjectEntity group, UserIdBoundary userId) {
		LinkedHashMap<String, String> linkedMap = new LinkedHashMap<>();
		linkedMap.put("superapp", userId.getSuperapp());
		linkedMap.put("email", userId.getEmail());

		return ((List<LinkedHashMap<String, String>>)this.converter
				.detailsToMap(group.getObjectDetails()).get("members"))
				.contains(linkedMap);
	}

	private void checkGroupData(SuperAppObjectBoundary group) {
		UserIdBoundary creatingUser = group.getCreatedBy().getUserId();
		Map<String, Object> objDetails = group.getObjectDetails();
		if (objDetails == null)
			throw new InvalidInputException("group must specify it's members");
		List<UserIdBoundary> groupMembers = (List<UserIdBoundary>)objDetails.get("members");

		if (groupMembers == null || groupMembers.isEmpty() ||
				!this.isUserInGroup(this.converter.toEntity(group), creatingUser))
			throw new InvalidInputException("group must contain members including its creator");

		SplitDebtBoundary[] debts = (SplitDebtBoundary[])this.showAllDebts(this.converter.toEntity(group));
		if (Arrays.stream(debts).anyMatch(member -> member != null && member.getDebt() < 0))
			throw new InvalidInputException("Cannot remove group member while any group members owe money");
	}

	private void checkTransactionData(SuperAppObjectBoundary transaction) {
		Map<String, Object> objDetails = transaction.getObjectDetails();
		if (objDetails == null)
			throw new InvalidInputException("transaction must specify amount (not less than or equal to 0)");

		Object amountData = objDetails.get("amount");
		if (amountData == null)
			throw new InvalidInputException("transaction must specify amount (not less than or equal to 0)");

		float amount = doubleToFloat(amountData.toString());
		if (amount <= 0) {
			transaction.setActive(false);
			throw new InvalidInputException("transaction amount must not be less than or equal to 0");
		}
	}

	private float doubleToFloat(String value) {
		try {
			return Double.valueOf(value).floatValue();
		} catch (Exception e) {
			throw new InvalidInputException("invalid amount");
		}
	}
}
