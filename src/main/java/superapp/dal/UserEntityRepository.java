package superapp.dal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import superapp.data.UserEntity;

import java.util.List;

@Repository
public interface UserEntityRepository extends CrudRepository<UserEntity, UserEntity.UserPK> {
    @Query(value = "SELECT * FROM USERS WHERE EMAIL=email;", nativeQuery = true)
    List<UserEntity> findByEmail(String email);

}