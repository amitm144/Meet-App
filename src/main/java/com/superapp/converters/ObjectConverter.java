package application.converters;
import java.util.Map;

//import org.springframework.stereotype.Component;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ObjectConverter{
    private ObjectMapper

    public ObjectConverter() {
        this.jackson = new ObjectMapper();
    }

    //to entity
    // to boundry
    // to entity as string
    // to boundry as map


//    public MessageEntity toEntity(DemoBoundary demo) {
//        MessageEntity rv = new MessageEntity ();
//
//        rv.setCurrentTimestamp(demo.getCurrentTimestamp());
//        rv.setDetails(toEntityAsString(demo.getDetails()));
//        if (demo.getName() != null) {
//            rv.setFirstName(demo.getName().getFirstName());
//            rv.setLastName(demo.getName().getLastName());
//        }else {
//            rv.setFirstName(null);
//            rv.setLastName(null);
//        }
//        rv.setId(Long.parseLong(demo.getId()));
//
//        if (demo.getImportant() != null) {
//            rv.setImportant(demo.getImportant());
//        }else {
//            rv.setImportant(true);
//        }
//
//        rv.setMessage(demo.getMessage());
//
//        rv.setStatus(demo.getStatus());
//        rv.setVersion(demo.getVersion());
//
//        return rv;
//    }

    public String toEntityAsString(Map<String, Object> details) {
        try {
            return this.jackson.writeValueAsString(details);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    public DemoBoundary toBoundary (MessageEntity entity) {
//        DemoBoundary rv = new DemoBoundary();
//
//        rv.setCurrentTimestamp(entity.getCurrentTimestamp());
//        rv.setDetails(toBoundaryAsMap(entity.getDetails()));
//        rv.setName(new NameBoundary(entity.getFirstName(), entity.getLastName()));
//        rv.setId("" + entity.getId());
//        rv.setImportant(entity.getImportant());
//        rv.setMessage(entity.getMessage());
//        rv.setStatus(entity.getStatus());
//        rv.setVersion(entity.getVersion());
//
//        return rv;
//    }

    private Map<String, Object> toBoundaryAsMap(String details) {
        try {
            return (Map<String, Object>)this.jackson
                    .readValue(details, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long toEntityAsLong(String id) {
        return Long.parseLong(id);
    }

}