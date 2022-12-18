package superapp.dal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import superapp.data.ObjectDetailsEntity;

@Repository
public interface ObjectDetailsEntityRepository extends CrudRepository<ObjectDetailsEntity, String> {
    @Query(value = "SELECT object_details_id FROM object_details ORDER BY object_details_id DESC LIMIT 1;",
            nativeQuery = true)
    String findLastId();
}
