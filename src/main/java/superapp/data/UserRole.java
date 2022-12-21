package superapp.data;

public enum UserRole {
    ADMIN,
    SUPERAPP_USER,
    MINIAPP_USER;

    public static boolean isValidRole(String role) {
        if (role == null)
            return false;
        try {
            UserRole.valueOf(role);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
