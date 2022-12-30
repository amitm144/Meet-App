package superapp.logic.concreteServices;

import org.apache.tomcat.jni.User;
import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.user.UserBoundary;
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
import superapp.util.exceptions.InvalidInputException;
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
		this.converter = new SuperAppObjectConverter();
	}

	@Override
	public void runCommand(String miniapp, SuperAppObjectIdWrapper targetObject, UserIdWrapper invokedBy, Map<String, Object> attributes, String commandCase) {
		userEntityRepository.findById(
						new UserEntity.UserPK(invokedBy.getUserId().getSuperapp(), invokedBy.getUserId().getEmail()))
				.orElseThrow(() ->  new NotFoundException("User not found"));
		SuperAppObjectEntity group = objectRepository.findById(
						(new SuperAppObjectEntity.SuperAppObjectId(targetObject.getObjectId().getSuperapp(), targetObject.getObjectId().getInternalObjectId())))
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
				this.payDebt(group, invokedBy);
				break;
			}
			default:
				throw new NotFoundException("Command Not Found");
		}
	}
	// TODO replace private Functions

	@Override
	public void handleObjectByType(SuperAppObjectBoundary object) {
		this.computeTransactionBalance(this.converter.toEntity(object));
	}

	@Override
	public void updateObjectDetails(SuperAppObjectEntity newTransaction) { //Case update Transaction
		computeTransactionBalance(newTransaction);
		objectRepository.save(newTransaction);
	}
	@Override
	public double showDebt(SuperAppObjectEntity group, UserIdWrapper user) {
		return group.getChildren()
				.stream()
				.filter(t -> t.getType().equals("Transaction"))
				.map(t -> (Map<UserIdWrapper, Double>) this.converter.detailsToMap(t.getObjectDetails()).get("allExpenses"))
				.mapToDouble(expenses -> expenses.get(user))
				.sum();
	}
	@Override
	public void payDebt(SuperAppObjectEntity group, UserIdWrapper user) {//Example : Payed user : 150,Not payed :-50,  Not payed :-50,Not payed :-50
		for (SuperAppObjectEntity trans : group.getChildren().stream().filter(t -> t.getType().equals("Transaction")).collect(Collectors.toList())) {

			Map<UserIdWrapper, Double> AllExpenses = (Map<UserIdWrapper, Double>) converter.detailsToMap(trans.getObjectDetails()).get("allExpenses");
			UserIdWrapper paid_user = (UserIdWrapper) converter.detailsToMap(trans.getObjectDetails()).get("paidUser");

			double userDebt = AllExpenses.get(user);
			if (userDebt <= 0)
				throw new RuntimeException("Only Users with debt can pay"); // For Example Trans owner will not able to pay due to his Negative Debt
			else {
				ComputeTransaction(user, trans, userDebt, paid_user, AllExpenses);//Example : Payed user : 100,Not payed :0,  Not payed :-50,Not payed :-50
			}
		}

	}
	private void ComputeTransaction(UserIdWrapper user, SuperAppObjectEntity trans, double userDebt, UserIdWrapper paid_user, Map<UserIdWrapper, Double> allExpenses) {
		double paidUserDebts = allExpenses.get(paid_user);
		allExpenses.put(paid_user, paidUserDebts + userDebt);
		allExpenses.put(user, 0.0);

		Map<String,Object> details =this.converter.detailsToMap(trans.getObjectDetails());
		details.put("allExpenses", allExpenses);
		trans.setObjectDetails(this.converter.detailsToString(details));

		if (allExpenses.get(paid_user) == 0) {
			trans.setActive(false);
		}
		this.objectRepository.save(trans);
	}
	public SuperAppObjectEntity computeTransactionBalance(SuperAppObjectEntity trans) { // total_compute_per_group
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
	public void removeTransaction(UserIdWrapper user, SuperAppObjectEntity group, SuperAppObjectEntity transaction) {
		Map<UserIdWrapper, Double> userDebt = (Map<UserIdWrapper, Double>)this.converter.detailsToMap(transaction.getObjectDetails()).get("userDebt");
		double originalPayment = (double) this.converter.detailsToMap(transaction.getObjectDetails()).get("originalPayment");
		if (userDebt.get(user) !=originalPayment)
            throw new CannotProcessException("Cannot close payment, one user or more already paid");
		objectRepository.delete(transaction);
    }

 

}
