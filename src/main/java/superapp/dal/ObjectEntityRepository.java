package superapp.dal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import superapp.data.ObjectEntity;

import java.util.Optional;

@Repository
public interface ObjectEntityRepository extends CrudRepository<ObjectEntity, String> {
    @Query(value = "SELECT * FROM OBJECTS WHERE SUPERAPP = ?1 AND OBJECT_ID = ?2", nativeQuery = true)
    Optional<ObjectEntity> findByCompositeId(String superapp, String id);
}