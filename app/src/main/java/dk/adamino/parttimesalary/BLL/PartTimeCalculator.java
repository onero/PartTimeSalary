package dk.adamino.parttimesalary.BLL;

import dk.adamino.parttimesalary.BE.ESalary;

import static dk.adamino.parttimesalary.BE.ESalary.FULL_TIME_HOURS_WEEKLY;

/**
 * Created by Adamino.
 */

public class PartTimeCalculator implements IPartTimeCalculator {


    @Override
    public double getPartTimeSalaryFromFullTimeSalary(double weeklyHours, double fullTimeSalary) {
        return (weeklyHours / FULL_TIME_HOURS_WEEKLY.getValue()) * fullTimeSalary;
    }

    @Override
    public double getPartTimeSalaryFromPartTimeHourRate(double partTimeMonthlyHours, double hourRate) {
        return partTimeMonthlyHours * hourRate;
    }

    @Override
    public double getPartTimeHourRateFromPartTimeSalary(double partTimeSalary, double partTimeMonthlyHours) {
        return partTimeSalary / partTimeMonthlyHours;
    }

    @Override
    public double getPartTimeHoursMonthlyFromPartTimeHoursWeekly(double partTimeHoursWeekly) {
        return ESalary.getMonthlyDividedByWeekly() * partTimeHoursWeekly;
    }
}
