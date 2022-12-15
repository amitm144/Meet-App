package superapp.dal;

import org.springframework.data.repository.CrudRepository;
import superapp.data.MiniAppCommandEntity;

public interface MiniAppCommandRepository extends CrudRepository<MiniAppCommandEntity, String> {
}
