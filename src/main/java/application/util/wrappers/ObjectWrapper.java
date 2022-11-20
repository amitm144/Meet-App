package application.util.wrappers;

import application.boundaries.user.UserIdBoundary;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ObjectIdWrapper.class, name = "objectId"),
        @JsonSubTypes.Type(value = UserIdWrapper.class, name = "userId"),
})
public interface ObjectWrapper {}