package superapp.dal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import superapp.data.MiniAppCommandEntity;

@Repository
public interface MiniAppCommandRepository extends CrudRepository<MiniAppCommandEntity, String> {
    @Query(value ="SELECT * FROM MINI_APP_COMMAND WHERE MINIAPP = ?1", nativeQuery=true)
    Iterable<MiniAppCommandEntity> findAllByMiniapp(String miniapp);
}
