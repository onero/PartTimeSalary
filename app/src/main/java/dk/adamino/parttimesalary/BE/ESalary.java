package dk.adamino.parttimesalary.BE;

/**
 * Created by Adamino.
 */

public enum ESalary {
    FULL_TIME_HOURS_WEEKLY(37),
    FULL_TIME_HOURS_MONTHLY(160.33);


    private double value;

    private ESalary(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public static double getMonthlyDividedByWeekly() {
        return FULL_TIME_HOURS_MONTHLY.getValue() / FULL_TIME_HOURS_WEEKLY.getValue();
    }

}
