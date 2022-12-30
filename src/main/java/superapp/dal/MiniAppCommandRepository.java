package superapp.dal;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import superapp.data.MiniAppCommandEntity;

import java.util.List;

@Repository
public interface MiniAppCommandRepository extends PagingAndSortingRepository<MiniAppCommandEntity, String> {
    @Query(value ="SELECT * FROM MINI_APP_COMMAND WHERE MINIAPP = ?1", nativeQuery=true)
    List<MiniAppCommandEntity> findAllByMiniapp(@Param("miniapp") String miniapp, Pageable page);
}
