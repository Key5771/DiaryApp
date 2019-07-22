package com.example.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.marcohc.robotocalendar.RobotoCalendarView;

import java.util.Date;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements RobotoCalendarView.RobotoCalendarListener {

    private RobotoCalendarView robotoCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        robotoCalendarView = findViewById(R.id.robotoCalendarPicker);

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
