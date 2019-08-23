package com.example.myapplication.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.myapplication.Database.TaskContract;
import com.example.myapplication.Database.TaskDbHelper;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;


public class CalendarFragment extends Fragment {

    private TaskDbHelper mHelper;

    private CalendarView calendarView;
    private float scale = 1.2f;
    public static final int calendarBackground = R.drawable.backscreen2;


    @Override
    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_calendar_fragment,container,false);
        mHelper = new TaskDbHelper(getContext());

        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
//        calendarView.setScaleX(scale);
        calendarView.setScaleY(scale);


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


    private void showAddItemDialog(Context c){
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Todo")
                .setMessage("What do you have to do?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
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

}

