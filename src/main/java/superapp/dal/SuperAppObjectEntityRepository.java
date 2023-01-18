package superapp.dal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import superapp.data.SuperAppObjectEntity;
import superapp.data.SuperappObjectPK;

import java.util.Date;

@Repository
public interface SuperAppObjectEntityRepository extends PagingAndSortingRepository<SuperAppObjectEntity, SuperappObjectPK> {
    public Page<SuperAppObjectEntity> findByType(@Param("type") String type, Pageable page);
    public Page<SuperAppObjectEntity> findByTypeAndActiveIsTrue(@Param("type") String type, Pageable page);
    public Page<SuperAppObjectEntity> findByAlias(@Param("alias") String alias, Pageable page);
    public Page<SuperAppObjectEntity> findByAliasAndActiveIsTrue(@Param("alias") String alias, Pageable page);
    public Page<SuperAppObjectEntity> findByAliasContaining(@Param("text") String text, Pageable page);
    public Page<SuperAppObjectEntity> findByActiveIsTrueAndAliasContaining(@Param("text") String text, Pageable page);
    public Page<SuperAppObjectEntity> findAllByActiveIsTrue(Pageable page);
    public Page<SuperAppObjectEntity> findByChildrenContaining(SuperAppObjectEntity child, Pageable page);
    public Page<SuperAppObjectEntity> findByChildrenContainingAndActiveIsTrue(SuperAppObjectEntity child, Pageable page);
    public Page<SuperAppObjectEntity> findByParentsContaining(SuperAppObjectEntity child, Pageable page);
    public Page<SuperAppObjectEntity> findByParentsContainingAndActiveIsTrue(SuperAppObjectEntity child, Pageable page);
    public Page<SuperAppObjectEntity> findAllByCreationTimestampAfter(@Param("creationTimestamp") Date creationTimestamp, Pageable page);
}