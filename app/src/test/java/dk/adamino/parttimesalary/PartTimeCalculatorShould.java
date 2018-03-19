package dk.adamino.parttimesalary;

import org.junit.Test;

import dk.adamino.parttimesalary.BE.ESalary;
import dk.adamino.parttimesalary.BLL.IPartTimeCalculator;
import dk.adamino.parttimesalary.BLL.PartTimeCalculator;

import static dk.adamino.parttimesalary.BE.ESalary.FULL_TIME_HOURS_WEEKLY;
import static org.junit.Assert.assertEquals;

/**
 * Created by Adamino.
 */

public class PartTimeCalculatorShould {
    private IPartTimeCalculator mPartTimeCalculator;

    public PartTimeCalculatorShould() {
        mPartTimeCalculator = new PartTimeCalculator();
    }

    @Test
    public void getPartTimeSalaryFromFullTimeSalary_withInputUnderFullTime() {
        double weeklyHours = 15;
        double fullTimeSalary = 25_000;

        double expectedResult = (weeklyHours / FULL_TIME_HOURS_WEEKLY.getValue()) * fullTimeSalary;
        double result = mPartTimeCalculator.getPartTimeSalaryFromFullTimeSalary(weeklyHours, fullTimeSalary);

        assertEquals(expectedResult, result, 0);
    }

    @Test
    public void returnFullTimeSalary_withInputAtFullTime() {
        double weeklyHours = 37;
        double fullTimeSalary = 25_000;

        double result = mPartTimeCalculator.getPartTimeSalaryFromFullTimeSalary(weeklyHours, fullTimeSalary);

        assertEquals(fullTimeSalary, result, 0);
    }

    @Test
    public void getPartTimeSalaryFromPartTimeHourRate() {
        double partTimeHoursMonthly = 15;
        double hourRate = 150;

        double expectedResult = partTimeHoursMonthly * hourRate;
        double result = mPartTimeCalculator.getPartTimeSalaryFromPartTimeHourRate(partTimeHoursMonthly, hourRate);

        assertEquals(expectedResult, result, 0);
    }

    @Test
    public void getPartTimeHoursMonthlyFromPartTimeHoursWeekly() {
        double partTimeHoursWeekly = 15;

        double expectedResult = ESalary.getMonthlyDividedByWeekly() * partTimeHoursWeekly;
        double result = mPartTimeCalculator.getPartTimeHoursMonthlyFromPartTimeHoursWeekly(partTimeHoursWeekly);

        assertEquals(expectedResult, result, 0);
    }

    @Test
    public void getPartTimeHourRateFromPartTimeSalary() {
        double partTimeSalary = 8_500;
        double partTimeMonthlyHours = 65;

        double expectedResult = partTimeSalary / partTimeMonthlyHours;
        double result = mPartTimeCalculator.getPartTimeHourRateFromPartTimeSalary(partTimeSalary, partTimeMonthlyHours);

        assertEquals(expectedResult, result, 0);
    }

    @Test
    public void getWeeklyHoursFromMonthlyHours() {
        double monthlyHours = 65;

        double expectedResult = monthlyHours / ESalary.getMonthlyDividedByWeekly();
        double result = mPartTimeCalculator.getWeeklyHoursFromMonthlyHours(monthlyHours);

        assertEquals(expectedResult, result, 0);
    }

    @Test
    public void getFullTimeSalaryFromHourlyRate() {
        double hourlyRate = 150;

        double expectedResult = hourlyRate * ESalary.FULL_TIME_HOURS_MONTHLY.getValue();
        double result = mPartTimeCalculator.getFullTimeSalaryFromHourlyRate(hourlyRate);

        assertEquals(expectedResult, result, 0);
    }
}
