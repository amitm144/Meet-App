package superapp.dal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import superapp.data.UserEntity;

@Repository
public interface UserEntityRepository extends CrudRepository<UserEntity, String> {
    @Query(value = "SELECT * FROM USERS WHERE EMAIL=email;", nativeQuery = true)
    UserEntity findByEmail(String email);
}