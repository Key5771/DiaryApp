package com.example.myapplication.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Database.TaskDbHelper;
import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDiaryActivity extends AppCompatActivity {
    private TaskDbHelper mHelper;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference titleRef = databaseReference.child("title");
    DatabaseReference contentRef = databaseReference.child("content");


    Button save_btn;
    EditText title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
//        mHelper = new TaskDbHelper(this);

        init();

//        save_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String get_title = String.valueOf(title.getText());
//                String get_content = String.valueOf(title.getText());
//                SQLiteDatabase db = mHelper.getWritableDatabase();
//                ContentValues values = new ContentValues();
//                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, get_title);
//                values.put(TaskContract.TaskEntry.COL_TASK_CONTENT, get_content);
//                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
//                db.close();
//
//
//                finish();
//            }
//        });
    }

    public void init() {
        title = (EditText) findViewById(R.id.edit_title);
        content = (EditText) findViewById(R.id.edit_content);
        save_btn = (Button) findViewById(R.id.save_button);
    }

    @Override
    protected void onStart(){
        super.onStart();

        save_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DiaryContent diaryContent = new DiaryContent(title.getText().toString(),content.getText().toString());
                databaseReference.child("message").push().setValue(diaryContent);

                finish();
            }
        });
    }

}
