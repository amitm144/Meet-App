package superapp.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import superapp.dal.UserEntityRepository;
import superapp.data.UserEntity;
import superapp.data.UserPK;
import superapp.data.UserRole;

import java.util.List;
import java.util.Optional;

@Service
public abstract class AbstractService {
    protected String superappName;
    protected  UserEntityRepository userEntityRepository;
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
    public final boolean isUserRole(String email,UserRole usersRole){
        List<UserEntity> userE = userEntityRepository.findByEmail(email);
        if(userE.size() ==0) return false;
        return userE.get(0).getRole().equals(usersRole);
    }
}
