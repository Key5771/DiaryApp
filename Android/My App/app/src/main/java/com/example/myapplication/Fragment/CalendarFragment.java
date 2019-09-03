package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Activity.AddDiaryActivity;
import com.example.myapplication.Activity.SimpleDetailActivity;
import com.example.myapplication.Database.TaskDbHelper;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class CalendarFragment extends Fragment {

    private TaskDbHelper mHelper;
    private SimpleDetailActivity simpleDetailActivity = new SimpleDetailActivity();
    private CalendarView calendarView;
    private float scale = 1.05f;
    private int select_year, select_month, select_day;
    final Context context = getActivity();

    @Override
    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_calendar_fragment,container,false);
        mHelper = new TaskDbHelper(getContext());

        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        calendarView.setScaleY(scale);

        calendarView.setClickable(true);
        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDetailActivity.setVisible(true);
            }
        });
//        calendarView.setOnLongClickListener(new View.OnLongClickListener(){
//            @Override
//            public void onLongClick(View view1){
//                LayoutInflater layoutInflater = LayoutInflater.from(context);
//                View todoView = layoutInflater.inflate(R.layout.todo_popup,null);
//
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//                alertDialogBuilder.setView(todoView);
//
//                final EditText todoText = (EditText) todoView.findViewById(R.id.todo);
//                alertDialogBuilder.setTitle("할일응 입력해주세요!");
//
//                alertDialogBuilder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                alertDialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//            }
//        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                select_day = dayOfMonth;
                select_month = month +1;
                select_year = year;

            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent transferIntent = new Intent(getActivity(), AddDiaryActivity.class);
                transferIntent.putExtra("year",select_year);
                transferIntent.putExtra("month",select_month);
                transferIntent.putExtra("day",select_day);
                startActivity(transferIntent);
            }
        });

        return view;
    }




}

