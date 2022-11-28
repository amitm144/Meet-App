package com.superapp.converters;

import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.superapp.boundaries.object.ObjectIdBoundary;
import com.superapp.boundaries.command.user.UserBoundary;
import com.superapp.boundaries.object.ObjectBoundary;
import com.superapp.data.ObjectEntity;
import com.superapp.data.UserEntity;
import com.superapp.data.UserRole;
import org.springframework.stereotype.Component;

@Component
public class ObjectConverter {

    private ObjectMapper jackson;

    public ObjectConverter(ObjectMapper jackson) {
        this.jackson = new ObjectMapper();
    }

    public String longToStr(Long id) {
        try {
            return jackson.writeValueAsString(id);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public ObjectEntity toEntity(ObjectBoundary obj) {
        ObjectEntity rv = new ObjectEntity();
        rv.setObjectId(Long.parseLong(obj.getObjectId().getInternalObjectId()));
        rv.setSuperApp(obj.getObjectId().getSuperApp());
        rv.setActive(obj.isActive()); // change to get ??
        rv.setAlias(obj.getAlias());
        rv.setObjectDetails(obj.getObjectDetails());
        rv.setType(obj.getType());
        rv.setCreatedBy(obj.getCreatedBy());
        rv.setCreationTimeStamp(obj.getCreationTimeStamp());

        return rv;
    }

    public ObjectBoundary toBoundary(ObjectEntity obj) {
        ObjectBoundary rv = new ObjectBoundary();
        rv.setObjectId(idEntityToBoundary(obj));
        rv.setActive(obj.isActive());
        rv.setAlias(obj.getAlias());
        rv.setObjectDetails(obj.getObjectDetails());
        rv.setType(obj.getType());
        rv.setCreatedBy(obj.getCreatedBy());
        rv.setCreationTimeStamp(obj.getCreationTimeStamp());

        return rv;
    }

    public ObjectIdBoundary idEntityToBoundary(ObjectEntity obj){
        ObjectIdBoundary rv = new ObjectIdBoundary();
        try {
            rv.setInternalObjectId(jackson.writeValueAsString(obj.getObjectId()));
            rv.setSuperApp(obj.getSuperApp());
            return rv;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
