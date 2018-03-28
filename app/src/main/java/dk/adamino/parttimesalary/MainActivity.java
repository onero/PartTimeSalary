package dk.adamino.parttimesalary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("####.##");

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

        try {
            setListeners();
        } catch (NumberFormatException nfe) {
            Log.e(TAG, nfe.getMessage());
        }

        setReadyToCalculate(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.trash) {
            clearAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearAll() {
        mFullTimeSalary.setText("");
        mFullTimeSalaryHasInput = false;
        mWeeklyHours.setText("");
        mWeeklyHoursHasInput = false;
        mMonthlyHours.setText("");
        mMonthlyHoursHasInput = false;
        mHourlyRate.setText("");
        mHourlyRateHasInput = false;
        mSalary.setText("");
        setReadyToCalculate(false);
    }

    private void setListeners() {
        mFullTimeSalary.addTextChangedListener(new SalaryTextWatcher(mFullTimeSalary));
        mWeeklyHours = findViewById(R.id.txtWeeklyHours);
        mWeeklyHours.addTextChangedListener(new SalaryTextWatcher(mWeeklyHours));
        mHourlyRate = findViewById(R.id.txtHourlyRate);
        mHourlyRate.addTextChangedListener(new SalaryTextWatcher(mHourlyRate));

        mMonthlyHours = findViewById(R.id.txtMonthlyHours);
        mMonthlyHours.addTextChangedListener(new SalaryTextWatcher(mMonthlyHours));
    }

    private class SalaryTextWatcher implements TextWatcher {

        private View view;

        private SalaryTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()) {
                case R.id.txtFullTimeSalary:
                    if (mFullTimeSalary.hasFocus() &&
                            mFullTimeSalary.length() > 0) {
                        Log.d(TAG, "FullTime has input");
                        mFullTimeSalaryHasInput = true;
                        // Make sure to clear hourly rate, as both should not be set!
                        mHourlyRate.setText("");
                        mHourlyRateHasInput = false;
                    } else {
                        mFullTimeSalaryHasInput = false;
                    }
                    break;
                case R.id.txtWeeklyHours:
                    if (mWeeklyHours.hasFocus() &&
                            mWeeklyHours.length() > 0) {
                        Log.d(TAG, "Weekly has input");
                        mWeeklyHoursHasInput = true;
                        // Calculate and update monthly hours
                        double monthlyHours = mPartTimeCalculator.calculateMonthlyHoursFromWeeklyHours(getWeeklyHours());
                        String monthlyHoursAsString = DECIMAL_FORMAT.format(monthlyHours);
                        mMonthlyHours.setText(monthlyHoursAsString);
                        mMonthlyHoursHasInput = true;
                    } else {
                        mWeeklyHoursHasInput = false;
                        mMonthlyHours.setText("");
                    }
                    break;
                case R.id.txtHourlyRate:
                    if (mHourlyRate.hasFocus() &&
                            mHourlyRate.length() > 0) {
                        Log.d(TAG, "Hourly has input");
                        mHourlyRateHasInput = true;
                        // Make sure to clear full time salary, as both should not be set!
                        mFullTimeSalary.setText("");
                        mFullTimeSalaryHasInput = false;
                    } else {
                        mHourlyRateHasInput = false;
                    }
                    break;
                case R.id.txtMonthlyHours:
                    if (mMonthlyHours.hasFocus() &&
                            mMonthlyHours.getText().length() > 0) {
                        Log.d(TAG, "Monthly has input");
                        mMonthlyHoursHasInput = true;
                        // Calculate and update weekly hours
                        double weeklyHours = mPartTimeCalculator.calculateWeeklyHoursFromMonthlyHours(getMonthlyHours());
                        String weeklyHoursAsString = DECIMAL_FORMAT.format(weeklyHours);
                        mWeeklyHours.setText(weeklyHoursAsString);
                        mWeeklyHoursHasInput = true;
                    } else {
                        mMonthlyHoursHasInput = false;
                    }
                    break;
            }
            setReadyToCalculate(checkIsReadyToCalculate());
        }
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
            monthlyHours = mPartTimeCalculator.calculateMonthlyHoursFromWeeklyHours(weeklyHours);
        } else {
            monthlyHours = getMonthlyHours();
        }
        hourlyRate = getHourlyRate();

        salary = mPartTimeCalculator.calculateFromMonthlyHoursAndHourRate(monthlyHours, hourlyRate);
        // Set useful information for other fields
        fullTimeSalary = mPartTimeCalculator.calculateFullTimeSalaryFromHourlyRate(hourlyRate);
        String fullTimeSalaryAsString = DECIMAL_FORMAT.format(fullTimeSalary);
        mFullTimeSalary.setText(fullTimeSalaryAsString);
        return salary;
    }

    private double calculatePartTimeSalaryFromFullTimeSalary() {
        double fullTimeSalary, weeklyHours, salary, monthlyHours, hourlyRate;
        fullTimeSalary = getFullTimeSalary();
        weeklyHours = getWeeklyHours();
        salary = mPartTimeCalculator.calculateFromWeeklyHoursAndFullTimeSalary(weeklyHours, fullTimeSalary);
        // Set useful information for other fields
        monthlyHours = mPartTimeCalculator.calculateMonthlyHoursFromWeeklyHours(weeklyHours);
        hourlyRate = mPartTimeCalculator.calculateHourRateFromPartTimeSalaryAndMonthlyHours(salary, monthlyHours);
        String hourlyRateAsString = DECIMAL_FORMAT.format(hourlyRate);
        mHourlyRate.setText(hourlyRateAsString);
        mHourlyRateHasInput = true;
        return salary;
    }

    /***
     * Get Full Time Salary input from user
     * @return salary as double
     */
    private double getFullTimeSalary() throws NumberFormatException {
        return Double.parseDouble(getFormattedDoubleAsString(mFullTimeSalary.getText().toString()));
    }

    /***
     * Get weekly hours input from user
     * @return hours as double
     */
    private double getWeeklyHours() throws NumberFormatException {
        return Double.parseDouble(getFormattedDoubleAsString(mWeeklyHours.getText().toString()));
    }

    /***
     * Get Hourly Rate input from user
     * @return rate as double
     */
    public double getHourlyRate() throws NumberFormatException {
        return Double.parseDouble(getFormattedDoubleAsString(mHourlyRate.getText().toString()));
    }

    /***
     * Get Monthly Hours input from user
     * @return hours as double
     */
    public double getMonthlyHours() throws NumberFormatException {
        return Double.parseDouble(getFormattedDoubleAsString(mMonthlyHours.getText().toString()));
    }

    /**
     * Solution for "," vs "." issue
     *
     * @param doubleAsString
     * @return correctly formatted double as String
     */
    private String getFormattedDoubleAsString(String doubleAsString) {
        return doubleAsString.replace(",", ".");
    }
}
