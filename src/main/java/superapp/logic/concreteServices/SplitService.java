package superapp.logic.concreteServices;

import superapp.boundaries.ExpensesBoundary;
import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.user.UserIdBoundary;
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
import java.util.stream.IntStream;

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

		ArrayList<SuperAppObjectEntity> allTransaction = new ArrayList<SuperAppObjectEntity>();
		allTransaction.addAll(group.getChildren());




		double debt = 0.0;
		for (SuperAppObjectEntity tran : allTransaction) {
			double temp = calculateTransactionUsersDebts(tran, user);
			if (temp <= 0)
				debt += (-1) * temp;
		}
		return debt;
	}


	@Override
	public Object showAllDebt(SuperAppObjectEntity group) {
		List<UserIdWrapper> allGroupUsers = (List<UserIdWrapper>)converter.detailsToMap(group.getObjectDetails()).get("allUsers");
		return allGroupUsers
				.stream()
				.collect(Collectors.toMap(user -> user, user -> showDebt(group, user)));
	}

	@Override
	public void payAllDebts(SuperAppObjectEntity group, UserIdWrapper payingUser) { //Example : Payed user : 150,Not payed :-50,  Not payed :-50,Not payed :-50
		List<SuperAppObjectEntity> allTransactions = group.getChildren().stream().collect(Collectors.toList());

		allTransactions.forEach(transaction -> ComputeTransaction(payingUser, transaction, group.getCreatedBy(),
				fromLinkedHashMapToListExpensesBoundary((ArrayList<Object>)converter.detailsToMap(transaction.getObjectDetails()).get("allExpenses"))));
	}







	private double calculateTransactionUsersDebts(SuperAppObjectEntity trans,UserIdWrapper user) {
		Map<String,Object> details = converter.detailsToMap(trans.getObjectDetails());
		List<ExpensesBoundary> allExpenses = fromLinkedHashMapToListExpensesBoundary((ArrayList<Object>)details.get("allExpenses"));


		ExpensesBoundary expense = allExpenses.stream().filter(tran -> tran.getUser().equals(user)).toList().get(0);

		return expense.getAmount();
	}


	@Override
	public SuperAppObjectEntity computeTransactionBalance(SuperAppObjectEntity trans) { // total_compute_per_group

		Map<String,Object> details = converter.detailsToMap(trans.getObjectDetails());
		List<ExpensesBoundary> allExpenses = fromLinkedHashMapToListExpensesBoundary((ArrayList<Object>)details.get("allExpenses")) ;
		Double originalPayment = (Double)details.get("originalPayment");

		allExpenses.forEach(user -> user.setAmount(user.getAmount() - originalPayment / allExpenses.size()));

		details.put("allExpenses", allExpenses);

		trans.setObjectDetails(converter.detailsToString(details));
		this.objectRepository.save(trans);

		return trans;
	}





	private ExpensesBoundary getUserExpenses(UserIdWrapper user, List<ExpensesBoundary> allExpenses)
	{
		return allExpenses.stream()
				.filter(u -> u.getUser().getUserId().equals(user.getUserId()))
				.collect(Collectors.toList()).get(0);
	}

	private void ComputeTransaction(UserIdWrapper payingUser,
									SuperAppObjectEntity transaction,
									UserIdWrapper creatingUser,
									List<ExpensesBoundary> allExpenses) {
		if(transaction.getActive() == false)
			throw new CannotProcessException("Cannot make a payment on a closed transaction");

		ExpensesBoundary createUserExpenses = getUserExpenses(creatingUser, allExpenses);
		ExpensesBoundary payingUserExpenses = getUserExpenses(payingUser, allExpenses);
		double userPayDebt =payingUserExpenses.getAmount();
		if (userPayDebt <= 0)
			throw new RuntimeException("Paying user has no debt to pay");
		createUserExpenses.setAmount(createUserExpenses.getAmount()+userPayDebt);
		payingUserExpenses.setAmount(0);
		Map<String,Object> details =this.converter.detailsToMap(transaction.getObjectDetails());
		details.put("allExpenses", allExpenses);
		transaction.setObjectDetails(this.converter.detailsToString(details));

		if (createUserExpenses.getAmount() == 0)
			transaction.setActive(false);

		this.objectRepository.save(transaction);
	}

	private List<ExpensesBoundary> getAllExpenses(SuperAppObjectEntity trans){
		Map<String,Object> details = converter.detailsToMap(trans.getObjectDetails());
		return  fromLinkedHashMapToListExpensesBoundary((ArrayList<Object>)details.get("allExpenses")) ;
	}

	private List<ExpensesBoundary> fromLinkedHashMapToListExpensesBoundary(ArrayList<Object> allExpenses) {

		return allExpenses
				.stream()
				.map(expense -> fromLinkedHashMapToExpensesBoundary((LinkedHashMap<String,Object>)expense))
				.toList();
	}

	private ExpensesBoundary fromLinkedHashMapToExpensesBoundary(LinkedHashMap<String,Object> expenses) {

		System.err.println(expenses);

		LinkedHashMap<String,String> userId  = (LinkedHashMap<String,String> )expenses.get("userId") ;
		if (userId == null)
			userId=((LinkedHashMap<String,String> )expenses.get("user"));


		return new ExpensesBoundary(fromLinkedHashMapToWrapperId(userId) ,(Double) expenses.get("amount"));
	}

	private UserIdWrapper fromLinkedHashMapToWrapperId(LinkedHashMap<String,String> linkedWrapperId) {


		return new UserIdWrapper(new UserIdBoundary(linkedWrapperId.get("superapp"), linkedWrapperId.get("email")));
	}

	@Override
	public void removeTransaction(UserIdWrapper user, SuperAppObjectEntity group, SuperAppObjectEntity transaction) {
		Map<UserIdWrapper, Double> userDebt = (Map<UserIdWrapper, Double>)this.converter.detailsToMap(transaction.getObjectDetails()).get("userDebt");
		double originalPayment = (double) this.converter.detailsToMap(transaction.getObjectDetails()).get("originalPayment");
		if (userDebt.get(user) !=originalPayment)
			throw new CannotProcessException("Cannot close payment, one user or more already paid");
		objectRepository.delete(transaction);
	}
	}
