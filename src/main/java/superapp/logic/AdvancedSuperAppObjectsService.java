package superapp.logic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.data.SuperAppObjectEntity;

import java.util.List;

public interface AdvancedSuperAppObjectsService extends SuperAppObjectsService {
    public SuperAppObjectBoundary updateObject(String objectSuperapp, String internalObjectId,
                                               SuperAppObjectBoundary update,
                                               String userSuperapp,String email);
    public void bindNewChild(String parentSuperapp, String parentObjectId,
                             SuperAppObjectIdBoundary newChild,
                             String userSuperapp, String email);
    public List<SuperAppObjectBoundary> getAllObjects(String userSuperapp, String email, int size , int page);

    public List<SuperAppObjectBoundary> getChildren(String objectSuperapp, String internalObjectId,
                                                    String userSuperapp, String email,
                                                    int size, int page);

    public List<SuperAppObjectBoundary> getParents(String objectSuperapp, String internalObjectId,
                                                   String userSuperapp, String email,
                                                   int size, int page);

    public List<SuperAppObjectBoundary> SearchObjectsByType(String type,String userSuperapp,
                                                            String email,int size, int page);

    public List<SuperAppObjectBoundary> SearchObjectsByExactAlias(String alias, String userSuperapp,
                                                                  String email, int size, int page);

    public SuperAppObjectBoundary getSpecificObject(String objectSuperapp, String internalObjectId,
                                                    String userSuperapp, String email);

    public List<SuperAppObjectBoundary> SearchObjectsByAliasContaining(String text, String userSuperapp,
                                                                                String email, int size, int page);
    public List<SuperAppObjectBoundary> getObjectsByCreationTimestamp(String creationEnum, String userSuperapp,
                                                                         String email, int size, int page);

    public void deleteAllObjects(String userSuperapp, String email);

}
