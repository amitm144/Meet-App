package superapp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import superapp.boundaries.user.UserBoundary;
import superapp.logic.concreteServices.UserService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Profile("staging")
public class HelperInitializer implements CommandLineRunner {
	private UserService users;

	@Autowired
	public HelperInitializer(UserService users) {
		super();
		this.users = users;
	}

	@Override
	public void run(String... args) throws Exception {
		// SUPERAPP_USER(s) and MINIAPP_USER(s) creation
		List<UserBoundary> usersList =
				IntStream
						.range(0, 2)
						.mapToObj(i -> {
							UserBoundary super_user = new UserBoundary(users.getSuperappName(), "user" + i + "@test.com",
									"SUPERAPP_USER", "user" + i, "avatar" + i);
							return this.users.createUser(super_user);
						})
						.collect(Collectors.toList());
		IntStream
				.range(0, 2)
				.mapToObj(i -> {
					UserBoundary mini_user = new UserBoundary(users.getSuperappName(), "user" + (i + 10) + "@test.com",
							"MINIAPP_USER", "user" + (i + 10), "avatar" + (i + 10));
					return this.users.createUser(mini_user);
				})
				.forEach(usersList::add);
		// ADMIN creation
		UserBoundary admin = new UserBoundary(users.getSuperappName(), "admin@test.com",
				"ADMIN", "admin", "avatar");
	}
}
