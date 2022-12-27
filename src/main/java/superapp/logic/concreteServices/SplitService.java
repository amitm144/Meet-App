package superapp.logic.concreteServices;

import superapp.converters.SuperAppObjectConverter;
import superapp.dal.SuperAppObjectEntityRepository;
import superapp.dal.UserEntityRepository;
import superapp.data.SuperAppObjectEntity;
import superapp.data.split.SplitGroup;
import superapp.data.UserEntity;
import superapp.data.split.SplitTransaction;
import superapp.logic.MiniappCommandFactory;
import superapp.logic.SplitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import superapp.util.wrappers.SuperAppObjectIdWrapper;
import superapp.util.wrappers.UserIdWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SplitService implements SplitsService, MiniappCommandFactory {

	private UserEntityRepository userEntityRepository;
	private SuperAppObjectEntityRepository objectRepository;
	private SuperAppObjectConverter converter ;


	@Autowired
	public SplitService(UserEntityRepository userEntityRepository, SuperAppObjectEntityRepository objectRepository) {
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
                .get();


        switch (commandCase) {

			case "openNewTransaction": {
				String description = (String) attributes.get("description");
				Double splitBalance = (Double) attributes.get("splitBalance");

				this.openNewTransaction(group, user, description, splitBalance);
				break;
			}
			case "removeTransaction": {
				String transId = (String) attributes.get("transactionId");
				SplitTransaction tran = null; // repo.get(tranId)
				this.removeTransaction(user, group, tran);
				break;
			}
			case "updateTransaction": {
				String transId = (String) attributes.get("transactionId");
				SplitTransaction tran = null; // repo.get(tranId)

				double updated_payment = (double) attributes.get("payment");
				this.updateTransaction(user, group, tran, updated_payment);
				break;
			}
			case "showDebt": {
				this.showDebt(group, user);
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





	@Override
	public void openNewTransaction(SuperAppObjectEntity group, UserEntity payedUser, String description, double splitbalance) {

		SplitTransaction trans = new SplitTransaction(group, payedUser, new Date(), description, splitbalance);
//		split_group.addNewTransaction(trans);//  Example : Payed user : 200,Not payed :0,  Not payed :0,Not payed :0
		computeTransactionBalance(group);//Example : Payed user : 150,Not payed :-50,  Not payed :-50,Not payed :-50
	}


    @Override
    public void removeTransaction(UserEntity user, SuperAppObjectEntity group, SplitTransaction transaction) {

        if (!user.equals(transaction.getUserPaid()))
            throw new RuntimeException("Only the payer can remove the transaction");
        if (transaction.getGroupDebts().get(user) != transaction.getOriginalPayment())
            throw new RuntimeException("Cannot close payment , Atleast one user has been paid");
        SplitGroup split_group = getGroupSplit(group);
        for (UserEntity trans_user : transaction.getGroupDebts().keySet())
            transaction.getGroupDebts().put(trans_user, 0.0);

        transaction.setOpen(false);
        split_group.getExpenses().remove(transaction);
    }

    @Override
    public void updateTransaction(UserEntity user, SuperAppObjectEntity group, SplitTransaction transaction, double updated_payment) {

        if (!user.equals(transaction.getUserPaid()))
            throw new RuntimeException("Only the payer can remove the transaction");
        if (transaction.getGroupDebts().get(user) != transaction.getOriginalPayment())
            throw new RuntimeException("Cannot close payment , Atleast one user has been paid");

        for (UserEntity trans_user : transaction.getGroupDebts().keySet())
            transaction.getGroupDebts().put(trans_user, 0.0);
        transaction.getGroupDebts().put(user, updated_payment);
        computeTransactionBalance(group);
    }
	@Override
	public double showDebt(SuperAppObjectEntity group, UserEntity user) {
		SplitGroup split_group = getGroupSplit(group);
		double allDebt = 0;
		for (SplitTransaction trans : split_group.getExpenses()) {
			if (trans.getGroupDebts().containsKey(user))
				allDebt += trans.getGroupDebts().get(user);
		}
		return allDebt;
	}

	@Override
	public void payDebt(SuperAppObjectEntity group, UserEntity user) {//Example : Payed user : 150,Not payed :-50,  Not payed :-50,Not payed :-50
		SplitGroup split_group = getGroupSplit(group);
		for (SplitTransaction trans : split_group.getExpenses()) {
			HashMap<UserEntity, Double> debt = trans.getGroupDebts();
			if (debt.get(user) != null && trans.isOpen()) {
				trans.ComputeBank(user); //Example : Payed user : 100,Not payed :0,  Not payed :-50,Not payed :-50
			}
		}
	}






	public void computeTransactionBalance(SuperAppObjectEntity group) { // total_compute_per_group
		List<UserEntity> allUsers = (List<UserEntity>) converter.toBoundary(group).getObjectDetails().get("allUsers");

		for (UserEntity user : allUsers) {
			for (SplitTransaction trans : group.getExpenses()) {
				HashMap<UserEntity, Double> debt = trans.getGroupDebts();
				double balance = debt.get(user);
				double new_balance = balance - trans.getOriginalPayment() / group.getGroup().getAllUsers().size();
				debt.put(user, new_balance);
			}
		}
	}






}
