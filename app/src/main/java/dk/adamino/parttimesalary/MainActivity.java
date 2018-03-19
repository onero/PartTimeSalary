package dk.adamino.parttimesalary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

import dk.adamino.parttimesalary.BLL.IPartTimeCalculator;
import dk.adamino.parttimesalary.BLL.PartTimeCalculator;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "DEBUG";

    private TextView mSalary, mWeeklyHours, mFullTimeSalary, mHourlyRate, mMonthlyHours;
    private Button mCalculate;

    private IPartTimeCalculator mPartTimeCalculator;
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    private boolean mFullTimeSalaryHasInput = false;
    private boolean mWeeklyHoursHasInput = false;
    private boolean mHourlyRateHasInput = false;
    private boolean mMonthlyHoursHasInput = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPartTimeCalculator = new PartTimeCalculator();

        mCalculate = findViewById(R.id.btnCalculate);

        mSalary = findViewById(R.id.txtSalary);

        mFullTimeSalary = findViewById(R.id.txtFullTimeSalary);

        setListeners();

        setReadyToCalculate(false);
    }

    private void setListeners() {
        mFullTimeSalary.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mFullTimeSalary.getText().length() > 0) {
                    Log.d(TAG, "FullTime has input");
                    mFullTimeSalaryHasInput = true;
                    // Make sure to clear hourly rate, as both should not be set!
                    mHourlyRate.setText("");
                    mHourlyRateHasInput = false;
                } else {
                    mFullTimeSalaryHasInput = false;
                }
                setReadyToCalculate(checkIsReadyToCalculate());
                return false;
            }
        });
        mWeeklyHours = findViewById(R.id.txtWeeklyHours);
        mWeeklyHours.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mWeeklyHours.getText().length() > 0) {
                    Log.d(TAG, "Weekly has input");
                    mWeeklyHoursHasInput = true;
                    // Calculate and update monthly hours
                    double monthlyHours = mPartTimeCalculator.getMonthlyHoursFromWeeklyHours(getWeeklyHours());
                    String monthlyHoursAsString = DECIMAL_FORMAT.format(monthlyHours);
                    mMonthlyHours.setText(monthlyHoursAsString);
                    mMonthlyHoursHasInput = true;
                } else {
                    mWeeklyHoursHasInput = false;
                }
                setReadyToCalculate(checkIsReadyToCalculate());
                return false;
            }
        });
        mHourlyRate = findViewById(R.id.txtHourlyRate);
        mHourlyRate.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mHourlyRate.getText().length() > 0) {
                    Log.d(TAG, "Hourly has input");
                    mHourlyRateHasInput = true;
                    // Make sure to clear full time salary, as both should not be set!
                    mFullTimeSalary.setText("");
                    mFullTimeSalaryHasInput = false;
                } else {
                    mHourlyRateHasInput = false;
                }
                setReadyToCalculate(checkIsReadyToCalculate());
                return false;
            }
        });

        mMonthlyHours = findViewById(R.id.txtMonthlyHours);
        mMonthlyHours.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mMonthlyHours.getText().length() > 0) {
                    Log.d(TAG, "Monthly has input");
                    mMonthlyHoursHasInput = true;
                    // Calculate and update weekly hours
                    double weeklyHours = mPartTimeCalculator.getWeeklyHoursFromMonthlyHours(getMonthlyHours());
                    String weeklyHoursAsString = DECIMAL_FORMAT.format(weeklyHours);
                    mWeeklyHours.setText(weeklyHoursAsString);
                    mWeeklyHoursHasInput = true;
                } else {
                    mMonthlyHoursHasInput = false;
                }
                setReadyToCalculate(checkIsReadyToCalculate());
                return false;
            }
        });
    }

    /**
     * Update calculation button to reflect calculation state
     *
     * @param value
     */
    private void setReadyToCalculate(boolean value) {
        clearSalary();
        mCalculate.setEnabled(value);
    }

    /***
     * Clear salary field
     */
    private void clearSalary() {
        mSalary.setText("");
    }

    /***
     * Check if conditions for making a valid calculations are true
     * @return
     */
    private boolean checkIsReadyToCalculate() {
        // Check for Part Time Salary From Full Time Salary
        if (mFullTimeSalaryHasInput && mWeeklyHoursHasInput) {
            return true;
        }

        // Check for Part Time Salary From Hourly Rate
        if (mWeeklyHoursHasInput && mHourlyRateHasInput ||
                mMonthlyHoursHasInput && mHourlyRateHasInput) {
            return true;
        }

        // Not enough information to calculate
        return false;
    }

    /***
     * Calculate salary, based on user input
     * @param view
     */
    public void onClick_Calculate(View view) {
        try {
            double salary = 0;
            if (checkFieldsForPartTimeSalaryFromFullTimeSalary()) {
                salary = calculatePartTimeSalaryFromFullTimeSalary();
            } else if (checkFieldsForPartTimeSalaryFromHourlyRate()) {
                salary = calculatePartTimeSalaryFromHourlyRate();
            }

            String salaryAsString = DECIMAL_FORMAT.format(salary);
            mSalary.setText(salaryAsString);
        } catch (Exception ex) {
            Log.e(TAG, "Exception while parsing value: " + ex.getMessage());
        }

    }

    private boolean checkFieldsForPartTimeSalaryFromHourlyRate() {
        return mWeeklyHoursHasInput && mHourlyRateHasInput ||
                mMonthlyHoursHasInput && mHourlyRateHasInput;
    }

    private boolean checkFieldsForPartTimeSalaryFromFullTimeSalary() {
        return mFullTimeSalaryHasInput && mWeeklyHoursHasInput;
    }

    private double calculatePartTimeSalaryFromHourlyRate() {
        double fullTimeSalary, weeklyHours, salary, monthlyHours, hourlyRate;

        if (mWeeklyHoursHasInput) {
            weeklyHours = getWeeklyHours();
            monthlyHours = mPartTimeCalculator.getMonthlyHoursFromWeeklyHours(weeklyHours);
        } else {
            monthlyHours = getMonthlyHours();
        }
        hourlyRate = getHourlyRate();

        salary = mPartTimeCalculator.getPartTimeSalaryFromPartTimeHourRate(monthlyHours, hourlyRate);
        // Set useful information for other fields
        fullTimeSalary = mPartTimeCalculator.getFullTimeSalaryFromHourlyRate(hourlyRate);
        String fullTimeSalaryAsString = DECIMAL_FORMAT.format(fullTimeSalary);
        mFullTimeSalary.setText(fullTimeSalaryAsString);
        return salary;
    }

    private double calculatePartTimeSalaryFromFullTimeSalary() {
        double fullTimeSalary, weeklyHours, salary, monthlyHours, hourlyRate;
        fullTimeSalary = getFullTimeSalary();
        weeklyHours = getWeeklyHours();
        salary = mPartTimeCalculator.getPartTimeSalaryFromFullTimeSalary(weeklyHours, fullTimeSalary);
        // Set useful information for other fields
        monthlyHours = mPartTimeCalculator.getMonthlyHoursFromWeeklyHours(weeklyHours);
        hourlyRate = mPartTimeCalculator.getPartTimeHourRateFromPartTimeSalary(salary, monthlyHours);
        String hourlyRateAsString = DECIMAL_FORMAT.format(hourlyRate);
        mHourlyRate.setText(hourlyRateAsString);
        mHourlyRateHasInput = true;
        return salary;
    }

    /***
     * Get Full Time Salary input from user
     * @return salary as double
     */
    private double getFullTimeSalary() {
        return Double.parseDouble(mFullTimeSalary.getText().toString());
    }

    /***
     * Get weekly hours input from user
     * @return hours as double
     */
    private double getWeeklyHours() {
        return Double.parseDouble(mWeeklyHours.getText().toString());
    }

    /***
     * Get Hourly Rate input from user
     * @return rate as double
     */
    public double getHourlyRate() {
        return Double.parseDouble(mHourlyRate.getText().toString());
    }

    /***
     * Get Monthly Hours input from user
     * @return hours as double
     */
    public double getMonthlyHours() {
        return Double.parseDouble(mMonthlyHours.getText().toString());
    }
}
