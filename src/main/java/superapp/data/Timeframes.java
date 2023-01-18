package superapp.data;

public enum Timeframes
{
    LAST_MINUTE(60),
    LAST_HOUR(60*60),
    LAST_DAY(60*60*24);

    private final int value;
    Timeframes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static boolean isValidTimeframes(String creationEnum) {
        if (creationEnum == null)
            return false;
        try {
            Timeframes.valueOf(creationEnum);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}


