package superapp.logic;

import superapp.boundaries.object.SuperAppObjectBoundary;

import java.util.List;

public interface AdvancedSuperAppObjectsService extends SuperAppObjectsService {

    public List<SuperAppObjectBoundary> getAllObjects(int size , int page);

    public List<SuperAppObjectBoundary> getChildren(String objectSuperapp, String internalObjectId, int size, int page);

    public List<SuperAppObjectBoundary> SearchObjectsByType(String type, int size, int page);

    public List<SuperAppObjectBoundary> SearchObjectsByExactAlias(String alias, int size, int page);
}
