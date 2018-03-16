package dk.adamino.parttimesalary.BLL;

/**
 * Created by Adamino.
 */

public interface IPartTimeCalculator {

    /***
     * Calculate PartTimeSalary from FullTimeSalary
     * @param weeklyHours
     * @param fullTimeSalary
     * @return PartTimeSalary as double
     */
    double getPartTimeSalaryFromFullTimeSalary(double weeklyHours, double fullTimeSalary);

    /***
     * Calculate PartTimeSalary from partTimeHourRate
     * @param partTimeMonthlyHours
     * @param hourRate
     * @return PartTimeSalary as double
     */
    double getPartTimeSalaryFromPartTimeHourRate(double partTimeMonthlyHours, double hourRate);

    /***
     * Get PartTimeHourRate from PartTimeSalary
     * @param partTimeSalary
     * @param partTimeMonthlyHours
     * @return PartTimeHourRate as double
     */
    double getPartTimeHourRateFromPartTimeSalary(double partTimeSalary, double partTimeMonthlyHours);

    /***
     * Calculate PartTimeHoursMonthly from PartTimeHoursWeekly
     * @param partTimeHoursWeekly
     * @return PartTimeHoursMonthly as double
     */
    double getPartTimeHoursMonthlyFromPartTimeHoursWeekly(double partTimeHoursWeekly);
}
