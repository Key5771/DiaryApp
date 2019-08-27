package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Fragment.CalendarFragment;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class AddDiaryActivity extends AppCompatActivity {
    
    Button save_btn;
    EditText title, content;
    TextView cur_year, cur_month, cur_day;

    private FirebaseFirestore firebaseFirestore;
    private EditText edit_title, edit_content;
    private CalendarFragment calendarFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        init();
        calendarFragment = new CalendarFragment();


        firebaseFirestore = FirebaseFirestore.getInstance();

        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_content = (EditText) findViewById(R.id.edit_content);
        cur_year = (TextView) findViewById(R.id.cur_year_tv);
        cur_month = (TextView) findViewById(R.id.cur_month_tv);
        cur_day = (TextView) findViewById(R.id.cur_day_tv);

        Intent intent = getIntent();

        int year = intent.getIntExtra("year", 1);
        int month = intent.getIntExtra("month", 1);
        int day = intent.getIntExtra("day", 1);


        cur_day.setText(String.valueOf(day));
        cur_month.setText(String.valueOf(month));
        cur_year.setText(String.valueOf(year));

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = edit_title.getText().toString();
                String content = edit_content.getText().toString();

                Map<String, String> user_diary = new HashMap<>();

                user_diary.put("date", String.valueOf(year)+"/"+String.valueOf(month)+"/"+String.valueOf(day));
                user_diary.put("title", title);
                user_diary.put("content", content);

                firebaseFirestore.collection("diarys").add(user_diary)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(AddDiaryActivity.this, "저장되었습니다!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                String error = e.getMessage();
                                Toast.makeText(AddDiaryActivity.this,"Error :" + error, Toast.LENGTH_SHORT).show();
                            }
                        });
                finish();
            }
        });

    }

    public void init() {
        title = (EditText) findViewById(R.id.edit_title);
        content = (EditText) findViewById(R.id.edit_content);
        save_btn = (Button) findViewById(R.id.save_button);
    }


}
