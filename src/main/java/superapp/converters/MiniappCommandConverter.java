package superapp.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.command.MiniAppCommandIdBoundary;
import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.data.MiniAppCommandEntity;
import superapp.util.wrappers.SuperAppObjectIdWrapper;
import superapp.util.wrappers.UserIdWrapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MiniappCommandConverter {

    private ObjectMapper jackson;

    public MiniappCommandConverter(){
        this.jackson = new ObjectMapper();
    }

    public MiniAppCommandEntity toEntity(MiniAppCommandBoundary miniApp) {
        MiniAppCommandEntity result = new MiniAppCommandEntity();
        result.setSuperApp(miniApp.getCommandId().getSuperapp());
        result.setMiniApp(miniApp.getCommandId().getMiniapp());
        result.setInternalCommandId(miniApp.getCommandId().getInternalCommandId());
        result.setCommand(miniApp.getCommand());
        result.setInvocationTimestamp(miniApp.getInvocationTimestamp());
        result.setInternalObjectId(miniApp.getTargetObject().getObjectId().getInternalObjectId());
        result.setEmail((miniApp.getInvokedBy()).getUserId().getEmail());
        result.setCommandAttributes(toEntityAsString(miniApp.getCommandAttributes()));
        return result;
    }
    public String toEntityAsString(Map<String, Object> attributes) {
        try {
            return this.jackson.writeValueAsString(attributes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private Map<String, Object> toBoundaryAsMap(String attributes) {
        try {
            return (Map<String, Object>)this.jackson.readValue(attributes, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public MiniAppCommandBoundary toBoundary(MiniAppCommandEntity miniappEntity) {
        MiniAppCommandBoundary result = new MiniAppCommandBoundary();
        result.setCommandId(new MiniAppCommandIdBoundary(miniappEntity.getMiniApp(),miniappEntity.getInternalCommandId()));
        result.getCommandId().setSuperapp(miniappEntity.getSuperApp());
        result.setCommand(miniappEntity.getCommand());
        result.setCommandAttributes(toBoundaryAsMap(miniappEntity.getCommandAttributes()));
        result.setInvokedBy(new UserIdWrapper(new UserIdBoundary(miniappEntity.getSuperApp(), miniappEntity.getEmail())));
        result.setTargetObject(new SuperAppObjectIdWrapper(new SuperAppObjectIdBoundary(miniappEntity.getSuperApp(), miniappEntity.getInternalObjectId())));
        result.setInvocationTimestamp(miniappEntity.getInvocationTimestamp());
        return result;
    }
}
