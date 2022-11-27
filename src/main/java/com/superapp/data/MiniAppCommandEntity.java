package com.superapp.data;

import com.superapp.boundaries.command.MiniAppCommandIdBoundary;
import com.superapp.util.wrappers.ObjectIdWrapper;
import com.superapp.util.wrappers.UserIdWrapper;

import java.util.Date;
import java.util.Map;

public class MiniAppCommandEntity {
    private MiniAppCommandIdBoundary commandId ;
    private String command;
    private ObjectIdWrapper targetObject;
    private Date invocationTimeStamp;
    private UserIdWrapper invokedBy;
    private Map<String, Object> commandAttributes;

}
