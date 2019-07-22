package com.example.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.marcohc.robotocalendar.RobotoCalendarView;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements RobotoCalendarView.RobotoCalendarListener {

    private RobotoCalendarView robotoCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        robotoCalendarView = findViewById(R.id.robotoCalendarPicker);
        Button markDayButton = findViewById(R.id.markDayButton);
        Button clearSelectedDayButton = findViewById(R.id.clearSelectedDayButton);

        markDayButton.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            Random random = new Random(System.currentTimeMillis());
            int style = random.nextInt(2);
            int daySelected = random.nextInt(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, daySelected);

            switch (style) {
                case 0:
                    robotoCalendarView.markCircleImage1(calendar.getTime());
                    break;
                case 1:
                    robotoCalendarView.markCircleImage2(calendar.getTime());
                    break;
                default:
                    break;
            }
        });

        clearSelectedDayButton.setOnClickListener(v -> robotoCalendarView.clearSelectedDay());

        robotoCalendarView.setRobotoCalendarListener(this);

        robotoCalendarView.setShortWeekDays(false);

        robotoCalendarView.showDateTitle(true);

        robotoCalendarView.setDate(new Date());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onDayClick(Date date) {
        Toast.makeText(this, "onDayClick : " + date, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDayLongClick(Date date) {
        Toast.makeText(this, "onDayLongClick: " + date, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRightButtonClick() {
        Toast.makeText(this, "onRightButtonClick!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLeftButtonClick() {
        Toast.makeText(this, "onLeftButtonClick!", Toast.LENGTH_SHORT).show();
    }

}
