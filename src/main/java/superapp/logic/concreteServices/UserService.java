package superapp.logic.concreteServices;

import superapp.boundaries.user.NewUserBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.converters.UserConverter;
import superapp.dal.UserEntityRepository;
import superapp.data.UserEntity;
import superapp.data.UserPK;
import superapp.data.UserRole;
import superapp.logic.AbstractService;
import superapp.logic.AdvancedUsersService;
import superapp.util.exceptions.*;
import superapp.util.EmailChecker;
import superapp.boundaries.user.UserBoundary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static superapp.data.UserRole.ADMIN;
import static superapp.util.Constants.*;

@Service
public class UserService extends AbstractService implements AdvancedUsersService {
    private UserEntityRepository userEntityRepository;
    private UserConverter converter;

    @Autowired
    public UserService(UserConverter converter,
                       UserEntityRepository userEntityRepository) {
        this.converter = converter;
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    @Transactional
    public UserBoundary createUser(UserBoundary user) {
        UserIdBoundary userId = user.getUserId();
        if (userId == null || userId.getEmail() == null ||
                !EmailChecker.isValidEmail(userId.getEmail()) ||
                user.getAvatar() == null || user.getUsername() == null ||
                user.getAvatar().isBlank() ||  user.getUsername().isBlank() ||
                !UserRole.isValidRole(user.getRole()))
            throw new InvalidInputException("Invalid User details");

        user.setSuperApp(this.superappName);
        Optional<UserEntity> userE = this.userEntityRepository.findById(new UserPK(userId.getSuperapp(), userId.getEmail()));
        if (userE.isPresent())
            throw new AlreadyExistsException("User already exists");

        this.userEntityRepository.save(this.converter.toEntity(user));
        return user;
    }

    @Override
    public UserBoundary createUser(NewUserBoundary newUser) {
        return createUser(new UserBoundary(newUser.getEmail(), newUser.getRole(),
                newUser.getUsername(), newUser.getAvatar()));
    }

    @Override
    @Transactional(readOnly = true)
    public UserBoundary login(String superapp, String userEmail) {
        if (!isValidSuperapp(superapp))
            throw new InvalidInputException("Invalid superapp");

        Optional<UserEntity> user = this.userEntityRepository.findById(new UserPK(superapp, userEmail));
        if (user.isEmpty())
            throw new NotFoundException("Unknown user");

        return this.converter.toBoundary(user.get());
    }

    @Override
    @Transactional
    public UserBoundary updateUser(String superapp, String userEmail, UserBoundary update) {
        if (!isValidSuperapp(superapp))
            throw new InvalidInputException("Invalid superapp");

        Optional<UserEntity> userOpt = this.userEntityRepository.findById(new UserPK(superapp, userEmail));
        if (userOpt.isEmpty())
            throw new NotFoundException("Unknown user");

        UserEntity user = userOpt.get();
        if (!user.getSuperapp().equals(this.superappName))
            throw new CannotProcessException("Cannot update this user - Wrong superapp");

        String newUserName = update.getUsername();
        String newAvatar = update.getAvatar();
        String newRole = update.getRole();

        if (newUserName != null)
            if (newUserName.isBlank())
                throw new InvalidInputException("Invalid username");
            else
                user.setUsername(newUserName);

        if (newAvatar != null)
            if (newAvatar.isBlank())
                throw new InvalidInputException("Invalid user avatar");
            else
                user.setAvatar(newAvatar);

        if (newRole != null) {
            try {
                user.setRole(UserRole.valueOf(newRole));
            } catch (IllegalArgumentException e) { throw new InvalidInputException("Illegal user role"); }
        }
        userEntityRepository.save(user);
        return this.converter.toBoundary(user);
    }

    @Override
    @Deprecated
    @Transactional(readOnly = true)
    public List<UserBoundary> getAllUsers() {
        throw new InvalidInputException(DEPRECATED_EXCEPTION);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserBoundary> getAllUsers(String userSuperapp, String email,int size,int page) {
        UserPK userId = new UserPK(userSuperapp, email);
        if (!this.isValidUserCredentials(userId, ADMIN, this.userEntityRepository))
            throw new ForbbidenOperationException(ADMIN_ONLY_EXCEPTION);

        Iterable<UserEntity> users = this.userEntityRepository
                .findAll(PageRequest.of(page,size, DEFAULT_SORTING_DIRECTION,"superapp","email"));
        return StreamSupport
                .stream(users.spliterator() , false)
                .map(this.converter::toBoundary)
                .collect(Collectors.toList());
    }

    @Override
    @Deprecated
    @Transactional
    public void deleteAllUsers() {
        throw new InvalidInputException(DEPRECATED_EXCEPTION);
    }

    @Override
    @Transactional
    public void deleteAllUsers(String userSuperapp, String email) {
        UserPK userId = new UserPK(userSuperapp, email);
        if (!this.isValidUserCredentials(userId, ADMIN, this.userEntityRepository))
            throw new ForbbidenOperationException(ADMIN_ONLY_EXCEPTION);

        this.userEntityRepository.deleteAll();
    }
}
