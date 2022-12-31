package superapp.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import superapp.dal.UserEntityRepository;
import superapp.data.UserEntity;
import superapp.data.UserPK;
import superapp.data.UserRole;

import java.util.Optional;

@Service
public abstract class AbstractService {
    protected String superappName;
    protected  UserEntityRepository userEntityRepository;
    public AbstractService(){};
    @Autowired
    public AbstractService( UserEntityRepository userEntityRepository)
    {
        this.userEntityRepository =userEntityRepository;
    }
    @Value("${spring.application.name}")
    public final void setSuperappName(String name) { this.superappName = name; }

    public final String getSuperappName() { return this.superappName; }

    public final boolean isValidSuperapp(String superapp) { return this.superappName.equals(superapp);
    }
    public final boolean isUserRole(String userSuperapp, String email,UserRole usersRole){
            Optional<UserEntity> userE = userEntityRepository.findById(new UserPK(userSuperapp, email));
            if (userE.isPresent() && userE.get().getRole().equals(usersRole))
                return true;
            return false;
    }
}
