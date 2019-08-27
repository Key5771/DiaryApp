package com.example.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

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

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                Toast.makeText(getActivity(),dayOfMonth+"/"+(month+1)+"/"+year,Toast.LENGTH_SHORT).show();
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

