package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddDiaryActivity extends AppCompatActivity {
    
    Button save_btn;
    EditText title, content;
    TextView cur_year, cur_month, cur_day;
    Switch show_swt;

    private FirebaseFirestore firebaseFirestore;
    private EditText edit_title, edit_content;
    private FirebaseAuth firebaseAuth;
    private String public_ch;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        init();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        DiaryContent diaryContent = new DiaryContent();


        Intent intent = getIntent();

        int year = intent.getIntExtra("year", 1);
        int month = intent.getIntExtra("month", 1);
        int day = intent.getIntExtra("day", 1);

        show_swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    public_ch = show_swt.getTextOn().toString();
                } else{
                    public_ch = show_swt.getTextOff().toString();
                }
            }
        });
        cur_day.setText(String.valueOf(day));
        cur_month.setText(String.valueOf(month));
        cur_year.setText(String.valueOf(year));

        Date currentTime = new Date();
        Date selectTime = new Date(year,month,day);



        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                diaryContent.user_id = user.getEmail();
                diaryContent.title = edit_title.getText().toString();
                diaryContent.content = edit_content.getText().toString();
                diaryContent.timestamp = currentTime.toString();
                diaryContent.select_timestamp = selectTime.toString();


//                String title = edit_title.getText().toString();
//                String content = edit_content.getText().toString();

                if(diaryContent.title.isEmpty() || diaryContent.content.isEmpty()){
                    Toast.makeText(AddDiaryActivity.this, "내용을 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, String> user_diary = new HashMap<>();
//                user_diary.put("date", String.valueOf(year)+"/"+String.valueOf(month)+"/"+String.valueOf(day));
//                user_diary.put("title", title);
//                user_diary.put("content", content);
                user_diary.put("show",public_ch);
                user_diary.put("user id",diaryContent.user_id);
                user_diary.put("select timestamp",diaryContent.select_timestamp);
                user_diary.put("timestamp",diaryContent.timestamp);
                user_diary.put("title",diaryContent.title);
                user_diary.put("content",diaryContent.content);




                firebaseFirestore.collection("Content")
                        .add(user_diary)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(AddDiaryActivity.this, "저장되었습니다!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                String error = e.getMessage();
                                Toast.makeText(AddDiaryActivity.this,"Error :" + error, Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });

    }

    public void init() {
        title = (EditText) findViewById(R.id.edit_title);
        content = (EditText) findViewById(R.id.edit_content);
        save_btn = (Button) findViewById(R.id.save_button);

        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_content = (EditText) findViewById(R.id.edit_content);
        cur_year = (TextView) findViewById(R.id.cur_year_tv);
        cur_month = (TextView) findViewById(R.id.cur_month_tv);
        cur_day = (TextView) findViewById(R.id.cur_day_tv);

        show_swt = (Switch) findViewById(R.id.show_swt);
    }


}

