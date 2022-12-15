package superapp.dal;

import org.springframework.data.repository.CrudRepository;
import superapp.data.ObjectEntity;

public interface ObjectEntityRepository extends CrudRepository<ObjectEntity, String> {
}