package superapp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.command.MiniAppCommandIdBoundary;
import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.boundaries.user.UserBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.logic.concreteServices.MiniAppCommandService;
import superapp.logic.concreteServices.SuperAppObjectService;
import superapp.logic.concreteServices.UserService;
import superapp.util.wrappers.UserIdWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Component // TODO initialize this object ONLY when testing manually the server
public class HelperInitializer implements CommandLineRunner {
	private MiniAppCommandService commands;
	private SuperAppObjectService objects;
	private UserService users;

	@Autowired
	public HelperInitializer(MiniAppCommandService commands, SuperAppObjectService objects, UserService users) {
		super();
		this.commands = commands;
		this.objects = objects;
		this.users = users;
	}

	@Override
	public void run(String... args) throws Exception {
		//user
		UserIdBoundary amitId =
				new UserIdBoundary(this.users.getSuperappName() ,"amit@test.com" );
		UserIdBoundary yuvalId =
				new UserIdBoundary(this.users.getSuperappName() ,"yuval@test.com" );

		UserIdWrapper amitIdWrapper =
				new UserIdWrapper(amitId);
		UserIdWrapper yuvalIdWrapper =
				new UserIdWrapper(yuvalId);

		UserBoundary amit =
				this.users.createUser(new UserBoundary(amitId,"SUPERAPP_USER","amit","avatar1"));
		UserBoundary yuval =
				this.users.createUser(new UserBoundary(yuvalId,"SUPERAPP_USER","yuval","avatar2"));

		List<UserBoundary> allUsers = new ArrayList<>();
		allUsers.add(amit);
		allUsers.add(yuval);


		//object transaction

		Map<String, Object> transactionDetails = new HashMap<String, Object>();
		Map<UserBoundary, Double> allExpanses = new HashMap<UserBoundary, Double>();
		allExpanses.put(amit,100.00);
		allExpanses.put(yuval,0.0);
		transactionDetails.put("allExpenses",allExpanses);
		transactionDetails.put("originalPayment",100.00);


		SuperAppObjectIdBoundary transId =
				new SuperAppObjectIdBoundary();


		SuperAppObjectBoundary trans =
				this.objects.createObject(new SuperAppObjectBoundary(transId,"Transaction","alias1",transactionDetails,amitIdWrapper));



		//object group

		Map<String, Object> groupDetails = new HashMap<String, Object>();
		groupDetails.put("allUsers",allUsers);
		groupDetails.put("title","split");

		SuperAppObjectIdBoundary groupId =
				new SuperAppObjectIdBoundary(amit.getUserId().getSuperapp(), "11");


		SuperAppObjectBoundary group =
				this.objects.createObject(new SuperAppObjectBoundary(groupId,"Group","alias1",groupDetails,amitIdWrapper));


		objects.bindNewChild(group.getObjectId().getSuperapp(),group.getObjectId().getInternalObjectId() , trans.getObjectId());

		//command showDebt
		MiniAppCommandIdBoundary showDebtId =
				new MiniAppCommandIdBoundary("Split");

		Map<String, Object> commandDetails = new HashMap<String, Object>();
		commandDetails.put("Transaction",trans);

		MiniAppCommandBoundary command1 =
				(MiniAppCommandBoundary) this.commands.invokeCommand(new MiniAppCommandBoundary
						(showDebtId,"payDebt",group.getObjectId(),amit.getUserId(),commandDetails));

	}
}
