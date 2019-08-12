package com.example.myapplication.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.Database.TaskContract;
import com.example.myapplication.Database.TaskDbHelper;
import com.example.myapplication.Fragment.CalendarFragment;
import com.example.myapplication.Fragment.DiaryFragment;
import com.example.myapplication.Fragment.FeelingFragment;
import com.example.myapplication.Fragment.SettingFragment;
import com.example.myapplication.R;
import com.marcohc.robotocalendar.RobotoCalendarView;


import java.util.ArrayList;
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
    private TaskDbHelper mHelper;
    private ArrayAdapter<String> DiaryFragment;

    private static final String TAG = "MainActivity";
//    private CalendarView calendarView = new CalendarView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 달력 뷰
//        robotoCalendarView = findViewById(R.id.robotoCalendarPicker);
//        robotoCalendarView.setRobotoCalendarListener(this);
//        robotoCalendarView.setShortWeekDays(false);
//        robotoCalendarView.showDateTitle(true);
//        robotoCalendarView.setDate(new Date());


        //DB 저장
        mHelper = new TaskDbHelper(this);
        SQLiteDatabase db = mHelper.getReadableDatabase();
    Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
            new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
            null, null, null, null, null);
    while(cursor.moveToNext()){
        int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
        Log.d(TAG, "Task: " + cursor.getString(idx));
    }
    cursor.close();
    db.close();


    //하단 메뉴 뷰
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

    // 일기 어댑터
//    private ListView mDiaryListView;

//    private void upDateUI(){
//        ArrayList<String> DiaryList = new ArrayList<>();
//        SQLiteDatabase db = mHelper.getReadableDatabase();
//        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
//                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
//                null, null, null, null,null);
//        while(cursor.moveToNext()){
//            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
//            DiaryList.add(cursor.getString(idx));
//        }

//        if(DiaryFragment == null) {
//            DiaryFragment = new ArrayAdapter<>(this, R.layout.item_diary, R.id.diary_title, DiaryList);
//            mDiaryListView.setAdapter(DiaryFragment);
//        } else{
//            DiaryFragment.clear();
//            DiaryFragment.addAll(DiaryList);
//            DiaryFragment.notifyDataSetChanged();
//        }

//        cursor.close();
//        db.close();
//    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void showAddItemDialog(Context c){
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Add a new diary")
                .setMessage("What did you do today?")
                .setView(taskEditText)
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }


    @Override
    public void onDayClick(Date date) {
        Toast.makeText(this, "onDayClick : " + date, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDayLongClick(Date date) {
//        Toast.makeText(this, "onDayLongClick: " + date, Toast.LENGTH_SHORT).show();
        showAddItemDialog(MainActivity.this);
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
