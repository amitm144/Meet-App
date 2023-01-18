package superapp.dal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import superapp.data.SuperAppObjectEntity;
import superapp.data.SuperappObjectPK;

import java.util.Date;


@Repository
public interface SuperAppObjectEntityRepository extends PagingAndSortingRepository<SuperAppObjectEntity, SuperappObjectPK> {
    public Page<SuperAppObjectEntity> findByType(@Param("type") String type, PageRequest page);
    public Page<SuperAppObjectEntity> findByAlias(@Param("alias") String alias, PageRequest page);
    public Page<SuperAppObjectEntity> findByAliasContaining(@Param("text") String text, PageRequest page);
    public Page<SuperAppObjectEntity> findAllByCreationTimestampAfter(@Param("creationTimestamp") Date creationTimestamp, PageRequest page);

}