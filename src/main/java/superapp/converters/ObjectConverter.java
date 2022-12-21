package superapp.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import superapp.boundaries.object.ObjectIdBoundary;
import superapp.boundaries.object.ObjectBoundary;
import superapp.data.ObjectEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ObjectConverter {

    private ObjectMapper mapper;

    public ObjectConverter() { this.mapper = new ObjectMapper(); }

    public ObjectEntity toEntity(ObjectBoundary obj) {
        ObjectEntity objEntity = new ObjectEntity();
        objEntity.setObjectId(obj.getObjectId().getInternalObjectId());
        objEntity.setSuperapp(obj.getObjectId().getSuperapp());
        objEntity.setActive(obj.getActive());
        objEntity.setAlias(obj.getAlias());
        objEntity.setObjectDetails(this.detailsToString(obj.getObjectDetails()));
        objEntity.setType(obj.getType());
        objEntity.setCreatedBy(obj.getCreatedBy().getUserId());
        objEntity.setCreationTimestamp(obj.getCreationTimestamp());

        return objEntity;
    }

    public ObjectBoundary toBoundary(ObjectEntity obj) {
        ObjectBoundary objBoundary = new ObjectBoundary();
        objBoundary.setObjectId(new ObjectIdBoundary(obj.getSuperapp(), obj.getObjectId()));
        objBoundary.setActive(obj.getActive());
        objBoundary.setAlias(obj.getAlias());
        objBoundary.setObjectDetails(this.detailsToMap(obj.getObjectDetails()));
        objBoundary.setType(obj.getType());
        objBoundary.setCreatedBy(obj.getCreatedBy());
        objBoundary.setCreationTimestamp(obj.getCreationTimestamp());

        return objBoundary;
    }

    public String detailsToString(Map<String, Object> objectDetails) {
        try {
            return mapper.writeValueAsString(objectDetails);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> detailsToMap(String details) {
        try {
            return (Map<String, Object>)this.mapper.readValue(details, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
