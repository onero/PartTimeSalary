package dk.adamino.parttimesalary.BLL;

import dk.adamino.parttimesalary.BE.ESalary;

import static dk.adamino.parttimesalary.BE.ESalary.FULL_TIME_HOURS_WEEKLY;

/**
 * Created by Adamino.
 */

public class PartTimeCalculator implements IPartTimeCalculator {


    @Override
    public double calculateFromWeeklyHoursAndFullTimeSalary(double weeklyHours, double fullTimeSalary) {
        return (weeklyHours / FULL_TIME_HOURS_WEEKLY.getValue()) * fullTimeSalary;
    }

    @Override
    public double calculateFromMonthlyHoursAndHourRate(double monthlyHours, double hourRate) {
        return monthlyHours * hourRate;
    }

    @Override
    public double calculateHourRateFromPartTimeSalaryAndMonthlyHours(double salary, double monthlyHours) {
        return salary / monthlyHours;
    }

    @Override
    public double calculateMonthlyHoursFromWeeklyHours(double weeklyHours) {
        return ESalary.getMonthlyDividedByWeekly() * weeklyHours;
    }

    @Override
    public double calculateWeeklyHoursFromMonthlyHours(double monthlyHours) {
        return monthlyHours / ESalary.getMonthlyDividedByWeekly();
    }

    @Override
    public double calculateFullTimeSalaryFromHourlyRate(double hourlyRate) {
        return hourlyRate * ESalary.FULL_TIME_HOURS_MONTHLY.getValue();
    }
}
