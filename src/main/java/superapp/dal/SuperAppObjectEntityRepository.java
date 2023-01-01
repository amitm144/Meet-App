package superapp.dal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import superapp.data.SuperAppObjectEntity;
import superapp.data.SuperAppObjectEntity.SuperAppObjectId;


@Repository
public interface SuperAppObjectEntityRepository extends PagingAndSortingRepository<SuperAppObjectEntity, SuperAppObjectId> {
    public Page<SuperAppObjectEntity> findByType(String type, String userSupperapp, String email, PageRequest page);
    public Page<SuperAppObjectEntity> findByAlias(String alias, String userSupperapp, String email, PageRequest page);
}