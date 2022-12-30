package superapp.logic.concreteServices;

import superapp.boundaries.ExpensesBoundary;
import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.converters.SuperAppObjectConverter;
import superapp.dal.SuperAppObjectEntityRepository;
import superapp.dal.UserEntityRepository;
import superapp.data.SuperAppObjectEntity;
import superapp.data.UserEntity;
import superapp.logic.ServicesFactory;
import superapp.logic.SplitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import superapp.util.exceptions.CannotProcessException;
import superapp.util.exceptions.NotFoundException;
import superapp.util.wrappers.SuperAppObjectIdWrapper;
import superapp.util.wrappers.UserIdWrapper;

import java.util.*;
import java.util.stream.Collectors;

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
	public void runCommand(String miniapp, SuperAppObjectIdWrapper targetObject,
						   UserIdWrapper invokedBy, Map<String, Object> attributes,
						   String commandCase) {
		this.userEntityRepository.findById(
						new UserEntity.UserPK(invokedBy.getUserId().getSuperapp(), invokedBy.getUserId().getEmail()))
				.orElseThrow(() ->  new NotFoundException("User not found"));
		SuperAppObjectEntity group = this.objectRepository.findById(
						(new SuperAppObjectEntity.SuperAppObjectId(targetObject.getObjectId().getSuperapp(),
								targetObject.getObjectId().getInternalObjectId())))
				.orElseThrow(() ->  new NotFoundException("group not found"));

		switch (commandCase) {
			case "showDebt": {
				this.showDebt(group, invokedBy);
				break;
			}
			case "showAllDebts": {
				this.showAllDebt(group);
				break;
			}
			case "payDebt": {
				this.payAllDebts(group, invokedBy);
				break;
			}
			default:
				throw new NotFoundException("Unknown command");
		}
	}
	@Override
	public void handleObjectByType(SuperAppObjectBoundary object) {
		this.computeTransactionBalance(this.converter.toEntity(object));
	}

	@Override
	public void updateObjectDetails(SuperAppObjectEntity newTransaction) { // TODO: update transaction case
		computeTransactionBalance(newTransaction);
		objectRepository.save(newTransaction);
	}

	@Override
	public double showDebt(SuperAppObjectEntity group, UserIdWrapper user) {
		return group.getChildren()
				.stream()
				.filter(t -> t.getType().equals("Transaction"))
				.map(t -> (Map<UserIdWrapper, Double>)this.converter.detailsToMap(t.getObjectDetails()).get("allExpenses"))
				.mapToDouble(expenses -> expenses.get(user))
				.sum();
	}

	@Override
	public void payAllDebts(SuperAppObjectEntity group, UserIdWrapper payingUser) { //Example : Payed user : 150,Not payed :-50,  Not payed :-50,Not payed :-50
		List<ExpensesBoundary> allExpenses = (List<ExpensesBoundary>) converter.detailsToMap(group.getObjectDetails()).get("allExpenses");
		List<SuperAppObjectEntity> allTransactions = group.getChildren().stream().collect(Collectors.toList());
		allTransactions.forEach(transaction -> ComputeTransaction(payingUser, transaction, group.getCreatedBy(), allExpenses));
	}
	@Override
	public SuperAppObjectEntity computeTransactionBalance(SuperAppObjectEntity trans) { // total_compute_per_group
		//TODO convert allexpeness
		Map<String,Object> details = converter.detailsToMap(trans.getObjectDetails());
		HashMap<UserIdWrapper, Double> allExpenses = (HashMap<UserIdWrapper,Double>) details.get("allExpenses");
		Double originalPayment = (Double) details.get("originalPayment");
//		for (UserBoundary user : allExpenses.keySet())
//			allExpenses.put(user, allExpenses.get(user) - originalPayment / allExpenses.keySet().size());

		Set<UserIdWrapper> allusers = allExpenses.keySet();
		allusers
				.stream()
				.map(user -> allExpenses.put(user, allExpenses.get(user) - originalPayment / allExpenses.keySet().size()))
				.collect(Collectors.toMap(user -> user, Double -> Double));

		details.put("allExpenses", allExpenses);
		trans.setObjectDetails(converter.detailsToString(details));
		this.objectRepository.save(trans);
		return trans;
	}

	@Override
	public Object showAllDebt(SuperAppObjectEntity group) {
		List<UserIdWrapper> allGroupUsers = (List<UserIdWrapper>)converter.detailsToMap(group.getObjectDetails()).get("allUsers");
		return allGroupUsers
				.stream()
				.collect(Collectors.toMap(user -> user, user -> showDebt(group, user)));
	}

	@Override
	public void removeTransaction(UserIdWrapper user, SuperAppObjectEntity group, SuperAppObjectEntity transaction) {
		Map<UserIdWrapper, Double> userDebt = (Map<UserIdWrapper, Double>)this.converter.detailsToMap(transaction.getObjectDetails()).get("userDebt");
		double originalPayment = (double) this.converter.detailsToMap(transaction.getObjectDetails()).get("originalPayment");
		if (userDebt.get(user) !=originalPayment)
            throw new CannotProcessException("Cannot close payment, one user or more already paid");
		objectRepository.delete(transaction);
    }

	private ExpensesBoundary getUserExpensess(UserIdWrapper user, List<ExpensesBoundary> allExpenses)
	{
		return allExpenses.stream()
				.reduce(u -> u.getUser().getUserId().equals(user.getUserId()))
				.collect(Collectors.toList()).get(0);
	}

	private void ComputeTransaction(UserIdWrapper payingUser,
									SuperAppObjectEntity transaction,
									UserIdWrapper creatingUser,
									List<ExpensesBoundary> allExpenses) {
		if(transaction.getActive() == false)
			throw new CannotProcessException("Cannot make a payment when a transaction is closed");
		ExpensesBoundary createUserExpenses = getUserExpensess(creatingUser, allExpenses);
		ExpensesBoundary payingUserExpenses = getUserExpensess(payingUser, allExpenses);
		double userPayDebt =payingUserExpenses.getAmount();
		if (userPayDebt <= 0)
			throw new RuntimeException("Only Users with debt can pay");
		createUserExpenses.setAmount(createUserExpenses.getAmount()+userPayDebt);
		payingUserExpenses.setAmount(0);
		Map<String,Object> details =this.converter.detailsToMap(transaction.getObjectDetails());
		details.put("allExpenses", allExpenses);
		transaction.setObjectDetails(this.converter.detailsToString(details));

		if (createUserExpenses.getAmount() == 0)
			transaction.setActive(false);

		this.objectRepository.save(transaction);
	}
}
