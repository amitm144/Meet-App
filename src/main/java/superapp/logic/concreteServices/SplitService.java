package superapp.logic.concreteServices;

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
import superapp.util.wrappers.SuperAppObjectIdWrapper;
import superapp.util.wrappers.UserIdWrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SplitService implements SplitsService, ServicesFactory {

	private UserEntityRepository userEntityRepository;
	private SuperAppObjectEntityRepository objectRepository;
	private SuperAppObjectConverter converter;

	@Autowired
	public SplitService(UserEntityRepository userEntityRepository, SuperAppObjectEntityRepository objectRepository) {
		super();//
		this.userEntityRepository = userEntityRepository;
		this.objectRepository = objectRepository;
		this.converter = new SuperAppObjectConverter();
	}

	@Override
	public void runCommand(String miniapp, SuperAppObjectIdWrapper targetObject, UserIdWrapper invokedBy, Map<String, Object> attributes, String commandCase) {
		UserEntity user = userEntityRepository.findById(
						new UserEntity.UserPK(invokedBy.getUserId().getSuperapp(), invokedBy.getUserId().getEmail()))
				.get();
		SuperAppObjectEntity group = objectRepository.findById(
						(new SuperAppObjectEntity.SuperAppObjectId(targetObject.getObjectId().getSuperapp(), targetObject.getObjectId().getInternalObjectId())))
				.get();// TODO Or else Throw
		switch (commandCase) {
			case "showDebt": {
				this.showDebt(group, user);
				break;
			}
			case "showAllDebts": {
				this.showAllDebt(group);
				break;
			}
			case "payDebt": {
				this.payDebt(group, user);
				break;
			}
			default:
				throw new RuntimeException("Command Not Found");
		}
	}
	// TODO replace private Functions
	@Override
	public void handleObjectByType(SuperAppObjectBoundary object) { //  SplitGroup
		if (object.getType() == "Transaction")
			 computeTransactionBalance(object);
	}
	@Override
	public void updateObjectDetails(SuperAppObjectEntity newTransaction) { //Case update Transaction
		computeTransactionBalance(this.converter.toBoundary(newTransaction));
		objectRepository.save(newTransaction);
	}
	@Override
	public double showDebt(SuperAppObjectEntity group, UserEntity user) {
		return group.getChildren()
				.stream()
				.filter(t -> t.getType().equals("Transaction"))
				.map(t -> (Map<UserEntity, Double>) this.converter.detailsToMap(t.getObjectDetails()).get("AllExpenses"))
				.mapToDouble(expenses -> expenses.get(user))
				.sum();
	}
	@Override
	public void payDebt(SuperAppObjectEntity group, UserEntity user) {//Example : Payed user : 150,Not payed :-50,  Not payed :-50,Not payed :-50
		for (SuperAppObjectEntity trans : group.getChildren().stream().filter(t -> t.getType().equals("Transaction")).collect(Collectors.toList())) {

			Map<UserEntity, Double> AllExpenses = (Map<UserEntity, Double>) converter.detailsToMap(trans.getObjectDetails()).get("AllExpenses");
			UserEntity paid_user = (UserEntity) converter.detailsToMap(trans.getObjectDetails()).get("paidUser");

			double userDebt = AllExpenses.get(user);
			if (userDebt <= 0)
				throw new RuntimeException("Only Users with debt can pay"); // For Example Trans owner will not able to pay due to his Negative Debt
			else {
				if (!ComputeTransaction(user, converter.toBoundary(trans), userDebt, paid_user, AllExpenses)) { //Example : Payed user : 100,Not payed :0,  Not payed :-50,Not payed :-50
					removeTransaction(paid_user,group,trans);
				}
			}
		}

	}
	private boolean ComputeTransaction(UserEntity user, SuperAppObjectBoundary trans, double userDebt, UserEntity paid_user, Map<UserEntity, Double> allExpenses) {
		boolean isOpen = true;
		double paidUserDebts = allExpenses.get(paid_user);
		allExpenses.put(paid_user, paidUserDebts + userDebt);
		allExpenses.put(user, 0.0);
		trans.getObjectDetails().replace("AllExpenses", allExpenses);
		if (allExpenses.get(paid_user) == 0) {
			trans.getObjectDetails().replace("isTransOpen", false);
			isOpen = false;
		}
		this.objectRepository.save(converter.toEntity(trans));
		return isOpen;
	}
	public SuperAppObjectBoundary computeTransactionBalance(SuperAppObjectBoundary trans) { // total_compute_per_group
		HashMap<UserEntity, Double> allExpenses = (HashMap<UserEntity, Double>) trans.getObjectDetails().get("AllExpenses");
		double originalPayment = (Double) trans.getObjectDetails().get("originalPayment");
		allExpenses.keySet()
				.stream()
				.map(user -> allExpenses.put(user, allExpenses.get(user) - originalPayment / allExpenses.keySet().size()))
				.collect(Collectors.toList());
		trans.getObjectDetails().replace("allExpenses", allExpenses);
		return trans;
	}
	@Override
	public Map<UserEntity, Double> showAllDebt(SuperAppObjectEntity group) {
		List<UserEntity> allGroupUsers = (List<UserEntity>)converter.detailsToMap(group.getObjectDetails()).get("allUsers");
		return allGroupUsers
				.stream()
				.collect(Collectors.toMap(user -> user, user -> showDebt(group, user)));
	}
	public void removeTransaction(UserEntity user, SuperAppObjectEntity group, SuperAppObjectEntity transaction) {
		Map<UserEntity, Double> userDebt = (Map<UserEntity, Double>)this.converter.detailsToMap(transaction.getObjectDetails()).get("userDebt");
		double originalPayment = (double) this.converter.detailsToMap(transaction.getObjectDetails()).get("originalPayment");
		if (userDebt.get(user) !=originalPayment)
            throw new RuntimeException("Cannot close payment, one user or more already paid");
		objectRepository.delete(transaction);
    }
}
