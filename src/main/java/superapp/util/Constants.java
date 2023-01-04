package superapp.util;

import org.springframework.data.domain.Sort;

public class Constants {
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_PAGE = "0";
    public static final Sort.Direction DEFAULT_SORTING_DIRECTION = Sort.Direction.ASC;

    public static final String DEPRECATED_EXCEPTION = "Method is Deprecated";
    public static final String MINIAPP_USER_ONLY_EXCEPTION = "Operation allowed for MINIAPP_USERs only";
    public static final String SUPERAPP_USER_ONLY_EXCEPTION = "Operation allowed for SUPERAPP_USERs only";
    public static final String SUPERAPP_MINIAPP_USERS_ONLY_EXCEPTION = "Operation allowed only for SUPERAPP or MINIAPP users";
    public static final String ADMIN_ONLY_EXCEPTION = "Operation allowed for ADMINs only";
}
