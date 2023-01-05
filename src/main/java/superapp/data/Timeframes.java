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
}


