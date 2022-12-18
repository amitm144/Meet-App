package superapp.dal;

import org.springframework.data.repository.CrudRepository;
import superapp.data.IdGeneratorEntity;

public interface IdGeneratorRepository extends CrudRepository<IdGeneratorEntity, Long> {}
