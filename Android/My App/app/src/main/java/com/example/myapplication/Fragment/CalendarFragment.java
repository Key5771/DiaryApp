package com.example.myapplication.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Activity.AddDiaryActivity;
import com.example.myapplication.Activity.SimpleDetailActivity;
import com.example.myapplication.Activity.SimpleDetailActivity;
import com.example.myapplication.Database.TaskDbHelper;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;


public class CalendarFragment extends Fragment {

    private TaskDbHelper mHelper;
    private SimpleDetailActivity simpleDetailActivity = new SimpleDetailActivity();
    private CalendarView calendarView;
    private float scale = 1.05f;


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


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent transferIntent = new Intent(getActivity(), AddDiaryActivity.class);
                startActivity(transferIntent);
            }
        });

        return view;
    }




}

