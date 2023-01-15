package superapp.logic.concreteServices;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.split.SplitDebtBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.converters.SuperAppObjectConverter;
import superapp.converters.UserConverter;
import superapp.dal.SuperAppObjectEntityRepository;
import superapp.dal.UserEntityRepository;
import superapp.data.SuperAppObjectEntity;
import superapp.data.SuperappObjectPK;
import superapp.data.UserPK;
import superapp.logic.MiniAppServices;
import superapp.logic.SplitsService;
import superapp.util.exceptions.CannotProcessException;
import superapp.util.exceptions.InvalidInputException;
import superapp.util.exceptions.NotFoundException;

import java.util.*;

import static superapp.data.ObjectTypes.*;
import static superapp.util.Constants.*;

@Service("Split")
public class SplitService implements SplitsService, MiniAppServices {
	private SuperAppObjectEntityRepository objectRepository;
	private UserEntityRepository userRepository;
	private SuperAppObjectConverter objectConverter;
	private UserConverter userConverter;
	private final String INVALID_AMOUNT_MESSAGE =  "Transaction must specify amount (number not less than or equal to 0)";
	private Log logger = LogFactory.getLog(SplitService.class);
	@Autowired
	public SplitService(SuperAppObjectEntityRepository objectRepository,
						UserEntityRepository userRepository,
						SuperAppObjectConverter objectConverter,
						UserConverter userConverter) {
		this.objectRepository = objectRepository;
		this.userRepository = userRepository;
		this.objectConverter = objectConverter;
		this.userConverter = userConverter;
	}

	@Override
	public void handleObjectByType(SuperAppObjectBoundary object) {
		String objectType = object.getType();
		if (!isValidObjectType(objectType))
			objectType = "";
		switch (objectType) {
			case ("Group") -> this.checkGroupData(object);
			case ("Transaction") -> this.checkTransactionData(object);
			default -> {
				logger.error("in handleObjectByType func - %s".formatted(UNKNOWN_OBJECT_EXCEPTION));
				throw new InvalidInputException(UNKNOWN_OBJECT_EXCEPTION);
			}
		}
	}

	@Override
	public Object runCommand(MiniAppCommandBoundary command) {
		SuperappObjectPK targetObjectKey = this.objectConverter.idToEntity(command.getTargetObject().getObjectId());
		UserIdBoundary invokedBy = command.getInvokedBy().getUserId();
		SuperAppObjectEntity group =
				this.objectRepository.findById(targetObjectKey)
					.orElseThrow(() -> new NotFoundException(VALUE_NOT_FOUND_EXCEPTION.formatted("Group")));
		if (!isUserInGroup(group, invokedBy)) {
			logger.error("in runCommand func - %s".formatted(USER_NOT_IN_GROUP_EXCEPTION));
			throw new InvalidInputException(USER_NOT_IN_GROUP_EXCEPTION);
		}
		if (!group.getActive()) {
			logger.error("in runCommand func - %s".formatted(EXECUTE_ON_INACTIVE_EXCEPTION));
			throw new InvalidInputException(EXECUTE_ON_INACTIVE_EXCEPTION.formatted("group"));
		}

		String commandCase = command.getCommand();
		switch (commandCase) {
			case "showDebt" -> {
				SuperappObjectPK objectId =
						new SuperappObjectPK(group.getSuperapp(), group.getObjectId());
				return this.showDebt(objectId, invokedBy);
			}
			case "showAllDebts" -> { return this.showAllDebts(group); }
			case "settleGroupDebts" -> {
				this.settleGroupDebts(group);
				return null;
			}
			default -> {
				logger.error("in runCommand func - %s".formatted(UNKNOWN_COMMAND_EXCEPTION));
				throw new NotFoundException(UNKNOWN_COMMAND_EXCEPTION);
			}
		}
	}

	@Override
	public void checkValidBinding(SuperAppObjectEntity parent, SuperAppObjectEntity child, UserPK userId) {
		if (!child.getType().equals(Transaction.name())) {
			logger.error("in checkValidBinding func - Unknown bind request (Split)");
			throw new CannotProcessException("Unknown bind request (Split)");
		}
		if (!parent.getType().equals(Group.name())) {
			logger.error("in checkValidBinding func - Transactions can only be bound to groups");
			throw new InvalidInputException("Transactions can only be bound to groups");
		}
		if (!isUserInGroup(parent, new UserIdBoundary(userId.getSuperapp(), userId.getEmail()))) {
			logger.error("in checkValidBinding func - Transactions can only be bound to users in the group");
			throw new CannotProcessException("Transactions can only be bound by users in the group");
		}
	}

	@Override
	public SplitDebtBoundary showDebt(SuperappObjectPK groupId, UserIdBoundary user) {
		Optional<SuperAppObjectEntity> groupOptional = this.objectRepository.findById(groupId);
		if (groupOptional.isEmpty())
			return null;

		SuperAppObjectEntity group = groupOptional.get();
		Set<SuperAppObjectEntity> transactions = group.getChildren();
		int totalMembers = ((List<UserIdBoundary>)this.objectConverter
				.detailsToMap(group.getObjectDetails())
				.get("members"))
				.size();
		if (totalMembers == 0) { // guarding against division by zero
			logger.error("in showDebt func - Group has no members");
			throw new CannotProcessException("Group has no members");
		}

		float totalPayments = 0, membersPayments = 0;
		for (SuperAppObjectEntity t : transactions) {
			if (!t.getActive()) // check if transaction has already been settled
				continue;

			float amount = doubleToFloat(this.objectConverter.detailsToMap(t.getObjectDetails()).get("amount").toString());
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
		this.userConverter.mapListToBoundaryList(
				(List<Map<String, String>>)this.objectConverter.detailsToMap(group.getObjectDetails()).get("members")
				).forEach(userId -> {
					SuperappObjectPK objectId = new SuperappObjectPK(group.getSuperapp(), group.getObjectId());
					allDebts.add(showDebt(objectId, userId));
				});
		return allDebts.toArray(new SplitDebtBoundary[0]);
	}

	@Override
	public void settleGroupDebts(SuperAppObjectEntity group) {
		group.getChildren().forEach(t -> t.setActive(false));
		logger.info("All debts set to not active");
	}

	private boolean isUserInGroup(SuperAppObjectEntity group, UserIdBoundary userId) {
		List<UserIdBoundary> members = this.userConverter.mapListToBoundaryList(
				(List<Map<String, String>>)this.objectConverter
						.detailsToMap(group.getObjectDetails())
						.get("members"));

		return (members != null && members.contains(userId));
	}

	private void checkGroupData(SuperAppObjectBoundary group) {
		UserIdBoundary creatingUser = group.getCreatedBy().getUserId();
		Map<String, Object> objDetails = group.getObjectDetails();
		if (objDetails == null) {
			logger.error("in checkGroupData func - Group must specify it's members");
			throw new InvalidInputException("Group must specify it's members");
		}
		List<UserIdBoundary> groupMembers =
				this.userConverter.mapListToBoundaryList((List<Map<String, String>>)objDetails.get("members"));

		if (groupMembers == null || groupMembers.isEmpty() || groupMembers.size() < 2 ||
				!this.isUserInGroup(this.objectConverter.toEntity(group), creatingUser)) {
			logger.error("in checkGroupData func - Group must contain at least two members including its creator");
			throw new InvalidInputException("Group must contain at least two members including its creator");
		}

		boolean missingMember = groupMembers
				.stream()
				.map(this.userConverter::idBoundaryToPK)
				.map(this.userRepository::findById)
				.anyMatch(Optional::isEmpty);
		if (missingMember) {
			logger.error("in checkGroupData func - One or more users in this group doesn't exist");
			throw new InvalidInputException("One or more users in this group doesn't exist");
		}


		SplitDebtBoundary[] debts = (SplitDebtBoundary[])this.showAllDebts(this.objectConverter.toEntity(group));
		if (Arrays.stream(debts).anyMatch(member -> member != null && member.getDebt() < 0)) {
			logger.error("in checkGroupData func - Cannot remove group members while any group member owes money");
			throw new InvalidInputException("Cannot remove group members while any group member owes money");
		}
	}

	private void checkTransactionData(SuperAppObjectBoundary transaction) {
		Map<String, Object> objDetails = transaction.getObjectDetails();
		if (objDetails == null) {
			logger.error("in checkTransactionData func - object details - %s".formatted(INVALID_AMOUNT_MESSAGE));
			throw new InvalidInputException(INVALID_AMOUNT_MESSAGE);
		}
		Object amountData = objDetails.get("amount");
		if (amountData == null) {
			logger.error("in checkTransactionData func - amountData - %s".formatted(INVALID_AMOUNT_MESSAGE));
			throw new InvalidInputException(INVALID_AMOUNT_MESSAGE);
		}

		float amount = doubleToFloat(amountData.toString());
		if (amount <= 0) {
			transaction.setActive(false);
			logger.error("in checkTransactionData func - amount - %s".formatted(INVALID_AMOUNT_MESSAGE));
			throw new InvalidInputException(INVALID_AMOUNT_MESSAGE);
		}
	}

	private float doubleToFloat(String value) {
		try {
			return Double.valueOf(value).floatValue();
		} catch (Exception e) {
			logger.error("in doubleToFloat func - %s".formatted(INVALID_AMOUNT_MESSAGE));
			throw new InvalidInputException(INVALID_AMOUNT_MESSAGE);
		}
	}
}
