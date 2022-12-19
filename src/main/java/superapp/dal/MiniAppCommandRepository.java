package superapp.dal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.data.MiniAppCommandEntity;
import superapp.data.ObjectEntity;

import java.util.Optional;

@Repository
public interface MiniAppCommandRepository extends CrudRepository<MiniAppCommandEntity, String> {
    @Query(value ="SELECT * FROM MINI_APP_COMMAND WHERE MINIAPP = ?1", nativeQuery=true)
    Iterable<MiniAppCommandEntity> findAllByMiniapp(String miniapp);
}
