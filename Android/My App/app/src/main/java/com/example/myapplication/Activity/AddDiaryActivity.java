package com.example.myapplication.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.Database.TaskContract;
import com.example.myapplication.Database.TaskDbHelper;
import com.example.myapplication.Fragment.CalendarFragment;
import com.example.myapplication.R;

public class AddDiaryActivity extends AppCompatActivity {
    private TaskDbHelper mHelper;

    Button save_btn;
    EditText title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        mHelper = new TaskDbHelper(this);

        title = (EditText) findViewById(R.id.edit_title);
        content = (EditText) findViewById(R.id.edit_content);
        save_btn = (Button) findViewById(R.id.save_button);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String get_title = String.valueOf(title.getText());
                String get_content = String.valueOf(title.getText());
                SQLiteDatabase db = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, get_title);
                values.put(TaskContract.TaskEntry.COL_TASK_CONTENT, get_content);
                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                db.close();

//                Intent backtoActivity = new Intent(AddDiaryActivity.this, CalendarFragment.class);
//                startActivity(backtoActivity);
            }
        });
    }


}
