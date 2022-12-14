package superapp.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.command.MiniAppCommandIdBoundary;
import superapp.boundaries.object.ObjectIdBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.data.MiniAppCommandEntity;
import superapp.util.wrappers.ObjectIdWrapper;
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
        MiniAppCommandEntity rv = new MiniAppCommandEntity();
        rv.setSuperApp(miniApp.getCommandId().getSuperapp());
        rv.setMiniApp(miniApp.getCommandId().getMiniapp());
        rv.setInternalCommandId(miniApp.getCommandId().getInternalCommandId());
        rv.setCommand(miniApp.getCommand());
        rv.setInvocationTimestamp(miniApp.getInvocationTimestamp());
        rv.setInternalObjectId(((ObjectIdWrapper)miniApp.getTargetObject()).getObjectId().getInternalObjectId());
        rv.setEmail((miniApp.getInvokedBy()).getUserId().getEmail());
        rv.setCommandAttributes(toEntityAsString(miniApp.getCommandAttributes()));
        return rv;
    }
    public String toEntityAsString(Map<String, Object> attributes) {
        try {
            return this.jackson.writeValueAsString(attributes);
        }catch (Exception e) {
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
        MiniAppCommandBoundary rv = new MiniAppCommandBoundary();
        rv.setCommandId(new MiniAppCommandIdBoundary(miniappEntity.getMiniApp(),miniappEntity.getInternalCommandId()));
        rv.getCommandId().setSuperapp(miniappEntity.getSuperApp());
        rv.setCommand(miniappEntity.getCommand());
        rv.setCommandAttributes(toBoundaryAsMap(miniappEntity.getCommandAttributes()));
        rv.setInvokedBy(new UserIdWrapper(new UserIdBoundary(miniappEntity.getSuperApp(), miniappEntity.getEmail())));
        rv.setTargetObject(new ObjectIdWrapper(new ObjectIdBoundary(miniappEntity.getSuperApp(), miniappEntity.getInternalObjectId())));
        rv.setInvocationTimestamp(miniappEntity.getInvocationTimestamp());
        return rv;
    }
}
