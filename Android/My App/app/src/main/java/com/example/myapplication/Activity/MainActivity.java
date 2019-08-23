package com.example.myapplication.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Database.TaskContract;
import com.example.myapplication.Database.TaskDbHelper;
import com.example.myapplication.Fragment.CalendarFragment;
import com.example.myapplication.Fragment.DiaryFragment;
import com.example.myapplication.Fragment.FeelingFragment;
import com.example.myapplication.Fragment.SettingFragment;
import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private DiaryFragment diaryFragment = new DiaryFragment();
    private FeelingFragment feelingFragment = new FeelingFragment();
    private SettingFragment settingFragment = new SettingFragment();
    private CalendarFragment calendarFragment = new CalendarFragment();
    private TaskDbHelper mHelper;
    private ArrayAdapter<String> DiaryFragment;
    private FloatingActionButton fab;
    private ListView listOfDiary;

    private static final String TAG = "MainActivity";
//    private CalendarView calendarView = new CalendarView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DB 저장
        mHelper = new TaskDbHelper(this);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_TASK_CONTENT},
                null, null, null, null, null);
        while(cursor.moveToNext()){
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            int index = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_CONTENT);
            Log.d(TAG, "Task: " + cursor.getString(idx));
            Log.d(TAG, "Content: " + cursor.getString(index));
        }
        cursor.close();
    db.close();


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

    // 일기 어댑터
    List<DiaryContent> diaryContentList = new LinkedList<>();
    private ListView mDiaryListView;


    private void upDateUI(){
        ArrayList<String> DiaryList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_TASK_CONTENT},
                null, null, null, null,null);
        while(cursor.moveToNext()){
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            int index = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_CONTENT);
            DiaryList.add(cursor.getString(idx));
            DiaryList.add(cursor.getString(index));
        }

        mDiaryListView = (ListView) findViewById(R.id.diary_list);

        if(DiaryFragment == null) {
            DiaryFragment = new ArrayAdapter<>(this, R.layout.diary_list_item, R.id.diary_item_title, DiaryList);
            DiaryFragment = new ArrayAdapter<>(this, R.layout.diary_list_item, R.id.diary_item_content, DiaryList);
            mDiaryListView.setAdapter(DiaryFragment);
        } else {
            DiaryFragment.clear();
            DiaryFragment.addAll(DiaryList);
            DiaryFragment.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    //삭제 기능
    public void deleteDiary(View view){
        View parent = (View) view.getParent();
        TextView diaryTextView = (TextView) parent.findViewById(R.id.diary_item_title);
        TextView diaryTextView2 = (TextView) parent.findViewById(R.id.diary_item_content);
        String diary = String.valueOf(diaryTextView.getText());
        String diary2 = String.valueOf(diaryTextView2.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE, TaskContract.TaskEntry.COL_TASK_TITLE + " = ?", new String[]{diary, diary2});
        db.close();
        upDateUI();

    }


}
