package superapp.data;

public enum Timeframes
{
    LAST_MINUTE(60),
    LAST_HOUR(360),
    LAST_DAY(8640);

    private final int minute;
    Timeframes(int minute) {
        this.minute = minute;
    }

    public int getMinute() {
        return minute;
    }

    public static boolean isValidTimeframes(String creationEnum) {
        if (creationEnum == null)
            return false;
        try {
            UserRole.valueOf(creationEnum);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}


