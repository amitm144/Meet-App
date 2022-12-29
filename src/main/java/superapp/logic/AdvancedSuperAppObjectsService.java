package superapp.logic;

import superapp.boundaries.object.SuperAppObjectBoundary;

import java.util.List;

public interface AdvancedSuperAppObjectsService extends SuperAppObjectsService {

    public List<SuperAppObjectBoundary> getAllObjects(String userSupperapp, String email, int size , int page);

    public List<SuperAppObjectBoundary> getChildren(String objectSuperapp, String internalObjectId,String userSupperapp, String email, int size, int page);

    public List<SuperAppObjectBoundary> SearchObjectsByType(String type,String userSupperapp, String email, int size, int page);

    public List<SuperAppObjectBoundary> SearchObjectsByExactAlias(String alias, String userSupperapp, String email, int size, int page);

    public SuperAppObjectBoundary getSpecificObject(String objectSuperapp, String internalObjectId, String userSupperapp, String email);

    }
