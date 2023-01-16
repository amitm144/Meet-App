package superapp.util;

import org.springframework.data.domain.Sort;

public class Constants {
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_PAGE = "0";
    public static final Sort.Direction DEFAULT_SORTING_DIRECTION = Sort.Direction.ASC;

    public static final String USER_NOT_IN_GROUP_EXCEPTION = "Invoking user is not part of this group";
    public static final String VALUE_NOT_FOUND_EXCEPTION = "%s not found";
    public static final String WRONG_OBJECT_EXCEPTION = "Target object of incorrect type";
    public static final String UNKNOWN_OBJECT_EXCEPTION = "Unknown object type";
    public static final String UNKNOWN_COMMAND_EXCEPTION = "Unknown command";
    public static final String EXECUTE_ON_INACTIVE_EXCEPTION = "Cannot execute commands on inactive %s";
    public static final String OBJECT_NOT_BOUND_EXCEPTION = "%s is not bound to any group";
    public static final String DEPRECATED_EXCEPTION = "Method is Deprecated";
    public static final String MINIAPP_USER_ONLY_EXCEPTION = "Operation allowed for MINIAPP_USERs only";
    public static final String SUPERAPP_USER_ONLY_EXCEPTION = "Operation allowed for SUPERAPP_USERs only";
    public static final String SUPERAPP_MINIAPP_USERS_ONLY_EXCEPTION = "Operation allowed only for SUPERAPP or MINIAPP users";
    public static final String ADMIN_ONLY_EXCEPTION = "Operation allowed for ADMINs only";
}
