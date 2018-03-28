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
    double calculateFromWeeklyHoursAndFullTimeSalary(double weeklyHours, double fullTimeSalary);

    /***
     * Calculate PartTimeSalary from partTimeHourRate
     * @param monthlyHours
     * @param hourRate
     * @return PartTimeSalary as double
     */
    double calculateFromMonthlyHoursAndHourRate(double monthlyHours, double hourRate);

    /***
     * Get PartTimeHourRate from PartTimeSalary and
     * @param salary
     * @param monthlyHours
     * @return PartTimeHourRate as double
     */
    double calculateHourRateFromPartTimeSalaryAndMonthlyHours(double salary, double monthlyHours);

    /***
     * Calculate PartTimeHoursMonthly from PartTimeHoursWeekly
     * @param weeklyHours
     * @return PartTimeHoursMonthly as double
     */
    double calculateMonthlyHoursFromWeeklyHours(double weeklyHours);

    /***
     * Calculate WeeklyHours from MonthlyHours
     * @param monthlyHours
     * @return
     */
    double calculateWeeklyHoursFromMonthlyHours(double monthlyHours);

    /***
     * Calculate FullTimeSalary from HourlyRate
     * @param hourlyRate
     * @return
     */
    double calculateFullTimeSalaryFromHourlyRate(double hourlyRate);
}
