package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.Fragment.CalendarFragment;
import com.example.myapplication.Fragment.DiaryFragment;
import com.example.myapplication.Fragment.FeelingFragment;
import com.example.myapplication.Fragment.SettingFragment;
import com.marcohc.robotocalendar.RobotoCalendarView;


import java.util.Date;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements RobotoCalendarView.RobotoCalendarListener {

    private RobotoCalendarView robotoCalendarView;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private DiaryFragment diaryFragment = new DiaryFragment();
    private FeelingFragment feelingFragment = new FeelingFragment();
    private SettingFragment settingFragment = new SettingFragment();
    private CalendarFragment calendarFragment = new CalendarFragment();
    private Fragment fragment = new Fragment();
//    private CalendarView calendarView = new CalendarView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        robotoCalendarView = findViewById(R.id.robotoCalendarPicker);

        robotoCalendarView.setRobotoCalendarListener(this);

        robotoCalendarView.setShortWeekDays(false);

        robotoCalendarView.showDateTitle(true);

        robotoCalendarView.setDate(new Date());


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (menuItem.getItemId()){
                    case R.id.calendar:{
                        transaction.replace(R.id.frame_layout, calendarFragment).commitAllowingStateLoss();
                        break;
                }
                    case R.id.diary:{
                        transaction.replace(R.id.frame_layout, diaryFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.mood:{
                        transaction.replace(R.id.frame_layout, feelingFragment).commitAllowingStateLoss();
                        break;
                    }

                    case R.id.setting:{
                        transaction.replace(R.id.frame_layout, settingFragment).commitAllowingStateLoss();
                        break;
                    }

                }
                return true;
            }
        });
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
