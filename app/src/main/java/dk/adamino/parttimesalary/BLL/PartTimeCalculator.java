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
    public double getPartTimeSalaryFromPartTimeHourRate(double monthlyHours, double hourRate) {
        return monthlyHours * hourRate;
    }

    @Override
    public double getPartTimeHourRateFromPartTimeSalary(double salary, double monthlyHours) {
        return salary / monthlyHours;
    }

    @Override
    public double getPartTimeHoursMonthlyFromPartTimeHoursWeekly(double weeklyHours) {
        return ESalary.getMonthlyDividedByWeekly() * weeklyHours;
    }

    @Override
    public double getWeeklyHoursFromMonthlyHours(double monthlyHours) {
        return monthlyHours / ESalary.getMonthlyDividedByWeekly();
    }

    @Override
    public double getFullTimeSalaryFromHourlyRate(double hourlyRate) {
        return hourlyRate * ESalary.FULL_TIME_HOURS_MONTHLY.getValue();
    }
}
