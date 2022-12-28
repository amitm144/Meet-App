package superapp.logic;

import superapp.boundaries.object.SuperAppObjectBoundary;
import superapp.data.SuperAppObjectEntity;

public interface SuperAppObjectFactory {
   public SuperAppObjectBoundary setObjectDetails(SuperAppObjectBoundary object);
   public SuperAppObjectEntity updateObjectDetails(SuperAppObjectEntity object);
}
