package superapp.dal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import superapp.data.SuperAppObjectEntity;
import superapp.data.SuperappObjectPK;


@Repository
public interface SuperAppObjectEntityRepository extends PagingAndSortingRepository<SuperAppObjectEntity, SuperappObjectPK> {
    public Page<SuperAppObjectEntity> findByType(@Param("type") String type, Pageable page);
    public Page<SuperAppObjectEntity> findByTypeAndActiveIsTrue(@Param("type") String type,
                                                                Pageable page);
    public Page<SuperAppObjectEntity> findByAlias(@Param("alias") String alias, Pageable page);
     public Page<SuperAppObjectEntity> findByAliasAndActiveIsTrue(@Param("alias") String alias, Pageable page);
    public Page<SuperAppObjectEntity> findByAliasContaining(@Param("text") String text, Pageable page);
    public Page<SuperAppObjectEntity> findByActiveIsTrueAndAliasContaining(@Param("text") String text, Pageable page);
    public Page<SuperAppObjectEntity> findAllByActiveIsTrue(Pageable page);



   // public Page<SuperAppObjectEntity> findAllByParentsContainsAndActiveIsTrue(@Param("objectId") SuperappObjectPK objectId, Pageable page);
  //  public Page<SuperAppObjectEntity> findAllByActiveIsTrueAndParentsAndObjectId(@Param("objectId") SuperappObjectPK objectId, Pageable page);

}