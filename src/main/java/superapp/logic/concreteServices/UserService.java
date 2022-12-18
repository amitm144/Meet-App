package superapp.logic.concreteServices;

import superapp.boundaries.user.NewUserBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.converters.UserConverter;
import superapp.data.UserEntity;
import superapp.data.UserRole;
import superapp.logic.AbstractService;
import superapp.util.EmailChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;

import superapp.boundaries.user.UserBoundary;
import superapp.logic.UsersService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService extends AbstractService implements UsersService {

    private Map<String, UserEntity> users; // { email: User }
    private UserConverter converter;

    @Autowired
    public UserService(UserConverter converter) {
        this.converter = converter;
    }

    @PostConstruct
    public void setup() {
        this.users = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public UserBoundary createNewUser(String superapp , NewUserBoundary newUser) {
        if (users.containsKey(newUser.getEmail()))
            throw new RuntimeException("User already exists");

        UserIdBoundary userId = new UserIdBoundary(superapp , newUser.getEmail());
        if (userId == null || userId.getEmail() == null || !EmailChecker.isValidEmail(userId.getEmail()))
            throw new RuntimeException("Invalid User details");

        UserBoundary user = new UserBoundary(userId, newUser.getRole(), newUser.getUsername(), newUser.getAvatar());

        users.put(user.getUserId().getEmail(), this.converter.toEntity(user));
        //TODO: add newly created user to DB
        return user;
    }

    @Override
    public UserBoundary login(String superapp, String userEmail) {
        if (!isValidSuperapp(superapp))
            throw new RuntimeException("Invalid superapp");

        UserEntity user = this.users.get(userEmail);
        if (user == null || !user.getSuperapp().equals(this.superappName) || !user.getEmail().equals(userEmail))
            throw new RuntimeException("Unknown user");

        return this.converter.toBoundary(user);
    }

    @Override
    public UserBoundary updateUser(String superapp, String userEmail, UserBoundary update) {
        if (!isValidSuperapp(superapp))
            throw new RuntimeException("Invalid superapp");

        UserEntity user = this.users.get(userEmail);
        if (user == null || !user.getSuperapp().equals(this.superappName) || !user.getEmail().equals(userEmail))
            throw new RuntimeException("Unknown user");

        String newUserName = update.getUsername();
        String newAvatar = update.getAvatar();
        String newRole = update.getRole();

        if (newUserName != null)
            user.setUsername(newUserName);

        if (newAvatar != null)
            user.setAvatar(newAvatar);

        if (newRole != null) {
            try {
                user.setRole(UserRole.valueOf(newRole));
            } catch (Exception ignored) { /* for now - ignore role mismatch */ }
        }

        if (update.getUserId() != null) { // TODO: FIX SO USER CANNOT UPDATE EMAIL
            String newEmail = update.getUserId().getEmail();
            if (newEmail != null) {
                user.setEmail(newEmail);
                this.users.remove(userEmail);
                this.users.put(newEmail, user);
            }
        }

        return this.converter.toBoundary(user); // TODO: update user in DB
    }

    @Override
    public List<UserBoundary> getAllUsers() {
       return this.users
               .values()
               .stream()
               .map(this.converter::toBoundary)
               .collect(Collectors.toList());
    }

    @Override
    public void deleteAllUsers() { this.users.clear(); } // TODO: clear all users from DB
}
