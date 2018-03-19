package dk.adamino.parttimesalary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dk.adamino.parttimesalary.BLL.IPartTimeCalculator;
import dk.adamino.parttimesalary.BLL.PartTimeCalculator;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "DEBUG";

    private boolean mFullTimeSalaryHasInput = false;
    private boolean mWeeklyHoursHasInput = false;
    private boolean mHourlyRateHasInput = false;
    private boolean mMonthlyHoursHasInput = false;

    private TextView mSalary, mWeeklyHours, mFullTimeSalary, mHourlyRate, mMonthlyHours;
    private Button mCalculate;
    private IPartTimeCalculator mPartTimeCalculator;

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
                    // Make sure to clear monthly hours, as both should not be set!
                    mMonthlyHours.setText("");
                    mMonthlyHoursHasInput= false;
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
                    // Make sure to clear weekly hours, as both should not be set!
                    mWeeklyHours.setText("");
                    mWeeklyHoursHasInput = false;
                } else {
                    mMonthlyHoursHasInput = false;
                }
                setReadyToCalculate(checkIsReadyToCalculate());
                return false;
            }
        });
    }

    private void setReadyToCalculate(boolean value) {
        clearSalary();
        mCalculate.setEnabled(value);
    }

    private void clearSalary() {
        mSalary.setText("");
    }

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
            double salary, weeklyHours, fullTimeSalary, hourlyRate, monthlyHours;

            // Check for Part Time Salary From Full Time Salary
            if (mFullTimeSalaryHasInput && mWeeklyHoursHasInput) {
                fullTimeSalary = getFullTimeSalary();
                weeklyHours = getWeeklyHours();
                salary = mPartTimeCalculator.getPartTimeSalaryFromFullTimeSalary(weeklyHours, fullTimeSalary);
                mSalary.setText(salary + "");
                // Set useful information for other fields
                monthlyHours = mPartTimeCalculator.getPartTimeHoursMonthlyFromPartTimeHoursWeekly(weeklyHours);
                mMonthlyHours.setText(monthlyHours + "");
                hourlyRate = mPartTimeCalculator.getPartTimeHourRateFromPartTimeSalary(salary,monthlyHours);
                mHourlyRate.setText(hourlyRate + "");
                return;
            }

            // Check for Part Time Salary From Hourly Rate
            if (mWeeklyHoursHasInput || mMonthlyHoursHasInput && mHourlyRateHasInput) {
                if (mWeeklyHoursHasInput) {
                    weeklyHours = getWeeklyHours();
                    monthlyHours = mPartTimeCalculator.getPartTimeHoursMonthlyFromPartTimeHoursWeekly(weeklyHours);
                    mMonthlyHours.setText(monthlyHours+ "");
                } else {
                    monthlyHours = getMonthlyHours();
                    // TODO ALH: Calculate weekly hours
                }
                hourlyRate = getHourlyRate();

                salary = mPartTimeCalculator.getPartTimeSalaryFromPartTimeHourRate(monthlyHours, hourlyRate);
                mSalary.setText(salary + "");

                // Set useful information for other fields
                //TODO ALH: Calculate FUll Time Salary!
                return;
            }
        } catch (Exception ex) {
            Log.e(TAG, "Exception while parsing value: " + ex.getMessage());
        }

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
