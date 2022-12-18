package superapp.logic.concreteServices;

import superapp.boundaries.user.UserIdBoundary;
import superapp.converters.UserConverter;
import superapp.dal.UserEntityRepository;
import superapp.data.UserEntity;
import superapp.data.UserEntity.UserPK;
import superapp.data.UserRole;
import superapp.logic.AbstractService;
import superapp.util.EmailChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import superapp.boundaries.user.UserBoundary;
import superapp.logic.UsersService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService extends AbstractService implements UsersService {

    private UserEntityRepository userEntityRepository;
    private UserConverter converter;

    @Autowired
    public UserService(UserConverter converter,
                       UserEntityRepository userEntityRepository) {
        this.converter = converter;
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public UserBoundary createUser(UserBoundary user) {
        UserIdBoundary userId = user.getUserId();
        if (userId == null || userId.getEmail() == null || !EmailChecker.isValidEmail(userId.getEmail()))
            throw new RuntimeException("Invalid User details");

        user.setSuperApp(this.superappName);
        Optional<UserEntity> userE = this.userEntityRepository.findById(new UserPK(userId.getSuperapp(), userId.getEmail()));
        if (userE.isPresent())
            throw new RuntimeException("User already exists");

        this.userEntityRepository.save(this.converter.toEntity(user));
        return user;
    }

    @Override
    public UserBoundary login(String superapp, String userEmail) {
        if (!isValidSuperapp(superapp))
            throw new RuntimeException("Invalid superapp");

        Optional<UserEntity> user = this.userEntityRepository.findById(new UserPK(superapp, userEmail));
        if (user.isEmpty())
            throw new RuntimeException("Unknown user");

        return this.converter.toBoundary(user.get());
    }

    @Override
    public UserBoundary updateUser(String superapp, String userEmail, UserBoundary update) {
        if (!isValidSuperapp(superapp))
            throw new RuntimeException("Invalid superapp");

        Optional<UserEntity> userOpt = this.userEntityRepository.findById(new UserPK(superapp, userEmail));
        if (userOpt.isEmpty())
            throw new RuntimeException("Unknown user");

        UserEntity user = userOpt.get();
        if (!user.getSuperapp().equals(this.superappName))
            throw new RuntimeException("Cannot update this user - Wrong superapp");

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
            } catch (IllegalArgumentException e) { /* for now - ignore role mismatch */ }
        }
        userEntityRepository.save(user);
        return this.converter.toBoundary(user);
    }

    @Override
    public List<UserBoundary> getAllUsers() {
        Iterable<UserEntity> users = this.userEntityRepository.findAll();
        return StreamSupport
                .stream(users.spliterator() , false)
                .map(this.converter::toBoundary)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllUsers() { this.userEntityRepository.deleteAll(); }
}
