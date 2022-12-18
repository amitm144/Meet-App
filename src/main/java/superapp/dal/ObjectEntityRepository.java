package superapp.dal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import superapp.data.ObjectEntity;

@Repository
public interface ObjectEntityRepository extends CrudRepository<ObjectEntity, String> {
    @Query(value = "SELECT object_id FROM object ORDER BY object_id DESC LIMIT 1;", nativeQuery = true)
    String findLastId();
}