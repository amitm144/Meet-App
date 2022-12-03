package com.superapp.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.superapp.boundaries.object.ObjectIdBoundary;
import com.superapp.boundaries.object.ObjectBoundary;
import com.superapp.data.ObjectEntity;
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

        ObjectEntity objEntity = new ObjectEntity();
        objEntity.setObjectId(Long.parseLong(obj.getObjectId().getInternalObjectId()));
        objEntity.setSuperApp(obj.getObjectId().getSuperApp());
        objEntity.setActive(obj.getActive()); //
        objEntity.setAlias(obj.getAlias());
        objEntity.setObjectDetails(obj.getObjectDetails());
        objEntity.setType(obj.getType());
        objEntity.setCreatedBy(obj.getCreatedBy());
        objEntity.setCreationTimeStamp(obj.getCreationTimeStamp());

        return objEntity;
    }

    public ObjectBoundary toBoundary(ObjectEntity obj) {

        ObjectBoundary objBoundary = new ObjectBoundary();
        objBoundary.setObjectId(idEntityToBoundary(obj));
        objBoundary.setActive(obj.getActive());
        objBoundary.setAlias(obj.getAlias());
        objBoundary.setObjectDetails(obj.getObjectDetails());
        objBoundary.setType(obj.getType());
        objBoundary.setCreatedBy(obj.getCreatedBy());
        objBoundary.setCreationTimeStamp(obj.getCreationTimeStamp());

        return objBoundary;
    }

    public ObjectIdBoundary idEntityToBoundary(ObjectEntity obj){

        ObjectIdBoundary objIdBoundary = new ObjectIdBoundary();
        try {
            objIdBoundary.setInternalObjectId(jackson.writeValueAsString(obj.getObjectId()));
            objIdBoundary.setSuperApp(obj.getSuperApp());
            return objIdBoundary;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
