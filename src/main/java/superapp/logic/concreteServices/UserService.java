package superapp.logic.concreteServices;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    private Log logger = LogFactory.getLog(UserService.class);

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
                !UserRole.isValidRole(user.getRole())) {
            this.logger.error("Invalid User details");
            throw new InvalidInputException("Invalid User details");
        }

        user.setSuperApp(this.superappName);
        Optional<UserEntity> userE = this.userEntityRepository.findById(new UserPK(userId.getSuperapp(), userId.getEmail()));
        if (userE.isPresent()) {
            this.logger.error("In createUser func - User already exists");
            throw new AlreadyExistsException("User already exists");
        }
        this.userEntityRepository.save(this.converter.toEntity(user));
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public UserBoundary login(String superapp, String userEmail) {
        if (!isValidSuperapp(superapp)) {
            this.logger.error("in login func - Invalid super App");
            throw new InvalidInputException("Invalid superapp");

        }
        Optional<UserEntity> user = this.userEntityRepository.findById(new UserPK(superapp, userEmail));
        if (user.isEmpty()) {
            this.logger.error("in login func - Unknown user");
            throw new NotFoundException("Unknown user");
        }
        this.logger.info("User Created successfully");
        return this.converter.toBoundary(user.get());
    }

    @Override
    @Transactional
    public UserBoundary updateUser(String superapp, String userEmail, UserBoundary update) {
        if (!isValidSuperapp(superapp)) {
            this.logger.error("in updateUser func - Invalid super App");
            throw new InvalidInputException("Invalid superapp");
        }
        Optional<UserEntity> userOpt = this.userEntityRepository.findById(new UserPK(superapp, userEmail));
        if (userOpt.isEmpty()) {
            this.logger.error("in updateUser func - Unknown User");
            throw new NotFoundException("Unknown user");
        }
        UserEntity user = userOpt.get();
        if (!user.getSuperapp().equals(this.superappName)) {
            this.logger.error("in updateUser func - Wrong super app");
            throw new CannotProcessException("Cannot update this user - Wrong superapp");
        }
        String newUserName = update.getUsername();
        String newAvatar = update.getAvatar();
        String newRole = update.getRole();

        if (newUserName != null)
            if (newUserName.isBlank()) {
                this.logger.error("in updateUser func - Invalid username");
                throw new InvalidInputException("Invalid username");
            }
            else
                user.setUsername(newUserName);

        if (newAvatar != null)
            if (newAvatar.isBlank()) {
                this.logger.error("in updateUser func - Invalid username");
                throw new InvalidInputException("Invalid user avatar");
            }
            else
                user.setAvatar(newAvatar);

        if (newRole != null) {
            try {
                user.setRole(UserRole.valueOf(newRole));
            } catch (IllegalArgumentException e) {
                this.logger.error("in updateUser func - Illegal user role");
                throw new InvalidInputException("Illegal user role"); }
        }
        userEntityRepository.save(user);
        this.logger.info("User Updated successfully");
        return this.converter.toBoundary(user);
    }

    @Override
    @Deprecated
    @Transactional(readOnly = true)
    public List<UserBoundary> getAllUsers() {
        this.logger.error("in getAllUsers func - calling to - %s".formatted(DEPRECATED_EXCEPTION));
        throw new InvalidInputException(DEPRECATED_EXCEPTION);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserBoundary> getAllUsers(String userSuperapp, String email,int size,int page) {
        UserPK userId = new UserPK(userSuperapp, email);
        if (!this.isValidUserCredentials(userId, ADMIN, this.userEntityRepository)) {
            this.logger.error("in getAllUsers func - %s".formatted(ADMIN_ONLY_EXCEPTION));
            throw new ForbbidenOperationException(ADMIN_ONLY_EXCEPTION);
        }

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
        this.logger.error("in deleteAllUsers func - calling to - %s".formatted(DEPRECATED_EXCEPTION));
        throw new InvalidInputException(DEPRECATED_EXCEPTION);
    }

    @Override
    @Transactional
    public void deleteAllUsers(String userSuperapp, String email) {
        UserPK userId = new UserPK(userSuperapp, email);
        if (!this.isValidUserCredentials(userId, ADMIN, this.userEntityRepository)) {
            this.logger.error("in deleteAllUsers func - %s".formatted(ADMIN_ONLY_EXCEPTION));
            throw new ForbbidenOperationException(ADMIN_ONLY_EXCEPTION);
        }

        this.userEntityRepository.deleteAll();
        this.logger.info("All Users deleted successfully");
    }
}
