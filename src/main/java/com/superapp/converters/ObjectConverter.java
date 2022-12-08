package com.superapp.converters;

//import com.fasterxml.jackson.databind.ObjectMapper;
import com.superapp.boundaries.object.ObjectIdBoundary;
import com.superapp.boundaries.object.ObjectBoundary;
import com.superapp.boundaries.data.ObjectEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ObjectConverter {

//    private ObjectMapper mapper;

    public ObjectConverter() {/* this.mapper = new ObjectMapper(); */ }

    public ObjectEntity toEntity(ObjectBoundary obj) {
        ObjectEntity objEntity = new ObjectEntity();
        objEntity.setObjectId(obj.getObjectId().getInternalObjectId());
        objEntity.setSuperapp(obj.getObjectId().getSuperapp());
        objEntity.setActive(obj.getActive());
        objEntity.setAlias(obj.getAlias());
        objEntity.setObjectDetails(obj.getObjectDetails());
        objEntity.setType(obj.getType());
        objEntity.setCreatedBy(obj.getCreatedBy());
        objEntity.setCreationTimestamp(obj.getCreationTimestamp());

        return objEntity;
    }

    public ObjectBoundary toBoundary(ObjectEntity obj) {
        ObjectBoundary objBoundary = new ObjectBoundary();
        objBoundary.setObjectId(new ObjectIdBoundary(obj.getSuperapp(), obj.getObjectId()));
        objBoundary.setActive(obj.getActive());
        objBoundary.setAlias(obj.getAlias());
        objBoundary.setObjectDetails(obj.getObjectDetails());
        objBoundary.setType(obj.getType());
        objBoundary.setCreatedBy(obj.getCreatedBy());
        objBoundary.setCreationTimestamp(new Date());
        return objBoundary;
    }
}
