package com.example.myapplication.Fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Activity.MainActivity;
import com.example.myapplication.CalendarView;
import com.example.myapplication.Database.TaskContract;
import com.example.myapplication.Database.TaskDbHelper;
import com.example.myapplication.R;
import com.marcohc.robotocalendar.RobotoCalendarView;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CalendarFragment extends Fragment implements RobotoCalendarView.RobotoCalendarListener {

    private RobotoCalendarView robotoCalendarView;
    private TaskDbHelper mHelper;
    Activity root = getActivity();


    @Override
    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_calendar_fragment,container,false);

        mHelper = new TaskDbHelper(getContext());

        robotoCalendarView = (RobotoCalendarView) view.findViewById(R.id.robotoCalendarPicker);
        robotoCalendarView.setRobotoCalendarListener(this);
        robotoCalendarView.setShortWeekDays(false);
        robotoCalendarView.showDateTitle(true);
        robotoCalendarView.setDate(new Date());

        return view;
    }


    private void showAddItemDialog(Context c){
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("일기 쓰기")
                .setMessage("오늘 뭐했나요?")
                .setView(taskEditText)
                .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
//                        db.insert(TaskContract.TaskEntry.TABLE, null, values);
                        db.close();
                    }
                })
                .setNegativeButton("취소", null)
                .create();
        dialog.show();
    }


    @Override
    public void onDayClick(Date date) {
        Toast.makeText(getActivity(), "onDayClick : " + date, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDayLongClick(Date date) {
//        Toast.makeText(getActivity(), "onDayLongClick: " + date, Toast.LENGTH_SHORT).show();
        showAddItemDialog(getContext());
    }

    @Override
    public void onRightButtonClick() {
        Toast.makeText(getActivity(), "onRightButtonClick!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLeftButtonClick() {
        Toast.makeText(getActivity(), "onLeftButtonClick!", Toast.LENGTH_SHORT).show();
    }

}

