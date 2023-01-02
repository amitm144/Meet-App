package superapp.logic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import superapp.dal.UserEntityRepository;
import superapp.data.UserEntity;
import superapp.data.UserPK;
import superapp.data.UserRole;
import superapp.util.exceptions.ForbiddenInsteadException;

import java.util.Optional;

@Service
public abstract  class AbstractService {
    protected String superappName;

    @Value("${spring.application.name}")
    public final void setSuperappName(String name) { this.superappName = name; }

    public final String getSuperappName() { return this.superappName; }

    public final boolean isValidSuperapp(String superapp) { return this.superappName.equals(superapp); }

    public final boolean isValidUserCredentials(UserPK userId, UserRole role,
                                                UserEntityRepository repository) {
        Optional<UserEntity> userE = repository.findById(userId);
        if (!(userE.isPresent() && userE.get().getRole().equals(role)))
            return false;
        return true;
    }
}
