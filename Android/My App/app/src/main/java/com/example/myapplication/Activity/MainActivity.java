package com.example.myapplication.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragment.CalendarFragment;
import com.example.myapplication.Fragment.DiaryFragment;
import com.example.myapplication.Fragment.FeelingFragment;
import com.example.myapplication.Fragment.SettingFragment;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private DiaryFragment diaryFragment = new DiaryFragment();
    private FeelingFragment feelingFragment = new FeelingFragment();
    private SettingFragment settingFragment = new SettingFragment();
    private CalendarFragment calendarFragment = new CalendarFragment();

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    //하단 메뉴 뷰
    BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.replace(R.id.frame_layout, calendarFragment).commitAllowingStateLoss();

    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (menuItem.getItemId()){
                case R.id.calendar:{
                    transaction.replace(R.id.frame_layout, calendarFragment).commit();
                    break;
                }
                case R.id.diary:{
                    transaction.replace(R.id.frame_layout, diaryFragment).commit();
                    break;
                }
                case R.id.mood:{
                    transaction.replace(R.id.frame_layout, feelingFragment).commit();
                    break;
                }
                case R.id.setting:{
                    transaction.replace(R.id.frame_layout, settingFragment).commit();
                    break;
                }
            }
            return true;
        }
    });

    }

}
