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

import java.util.HashMap;
import java.util.Map;

@Component // TODO fix to initialize object ONLY when testing manually the server
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
		UserIdBoundary userId1 =
				new UserIdBoundary(this.users.getSuperappName() ,"user1@test.com" );
		UserIdBoundary userId2 =
				new UserIdBoundary(this.users.getSuperappName() ,"user2@test.com" );

		UserIdWrapper userIdWrapper1 =
				new UserIdWrapper(userId1);
		UserIdWrapper userIdWrapper2 =
				new UserIdWrapper(userId2);

		UserBoundary user1 =
				this.users.createUser(new UserBoundary(userId1,"SUPERAPP_USER","user1","avatar1"));
		UserBoundary user2 =
				this.users.createUser(new UserBoundary(userId2,"SUPERAPP_USER","user2","avatar2"));

		//object
		Map<String, Object> objectDetails = new HashMap<String, Object>();
		objectDetails.put("Object attribute1",1);
		objectDetails.put("Object attribute2",2);

		SuperAppObjectIdBoundary objId1 =
				new SuperAppObjectIdBoundary(user1.getUserId().getSuperapp(), "11");
		SuperAppObjectIdBoundary objId2 =
				new SuperAppObjectIdBoundary(user2.getUserId().getSuperapp(), "12");

		SuperAppObjectBoundary obj1 =
				this.objects.createObject(new SuperAppObjectBoundary(objId1,"object1","alias1",objectDetails,userIdWrapper1));
		SuperAppObjectBoundary obj2 =
				this.objects.createObject(new SuperAppObjectBoundary(objId2,"object2","alias2",objectDetails,userIdWrapper2));

		//command
		MiniAppCommandIdBoundary commandId1 =
				new MiniAppCommandIdBoundary("miniApp1","21");
		MiniAppCommandIdBoundary commandId2 =
				new MiniAppCommandIdBoundary("miniApp2","22");

		Map<String, Object> commandDetails = new HashMap<String, Object>();
		commandDetails.put("command attribute1",1);
		commandDetails.put("command attribute2",2);

		MiniAppCommandBoundary command1 =
				(MiniAppCommandBoundary) this.commands.invokeCommand(new MiniAppCommandBoundary(commandId1,"test1",objId1,userId1,commandDetails));
		MiniAppCommandBoundary command2 =
				(MiniAppCommandBoundary) this.commands.invokeCommand(new MiniAppCommandBoundary(commandId2,"test2",objId2,userId2,commandDetails));
	}
}
