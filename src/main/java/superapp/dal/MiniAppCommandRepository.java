package superapp.dal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import superapp.data.MiniAppCommandEntity;

@Repository
public interface MiniAppCommandRepository extends CrudRepository<MiniAppCommandEntity, String> {
}
