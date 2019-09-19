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

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class AddDiaryActivity extends AppCompatActivity {
    
    Button save_btn;
    EditText title, content;
    TextView date_tv;
    Switch show_swt;

    private FirebaseFirestore firebaseFirestore;
    private EditText edit_title, edit_content;
    private FirebaseAuth firebaseAuth;
    private Boolean show_check;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        init();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        DiaryContent diaryContent = new DiaryContent();


        Intent intent = getIntent();

        int year = intent.getIntExtra("year", 1);
        int month = intent.getIntExtra("month", 1);
        int day = intent.getIntExtra("day", 1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

        Date currentTime = Calendar.getInstance().getTime();

//        String currentTime = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(new Date());

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        Date selectTime = cal.getTime();
        date_tv.setText(dateFormat.format(selectTime));


        FirebaseUser user = firebaseAuth.getCurrentUser();

        show_check = show_swt.isChecked();
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                diaryContent.user_id = user.getEmail();
                diaryContent.title = edit_title.getText().toString();
                diaryContent.content = edit_content.getText().toString();
                diaryContent.timestamp = currentTime;
                diaryContent.select_timestamp = selectTime;


                if(diaryContent.title.isEmpty() || diaryContent.content.isEmpty()){
                    Toast.makeText(AddDiaryActivity.this, "내용을 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> user_diary = new HashMap<>();
                user_diary.put("show",show_check);
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

        date_tv = (TextView) findViewById(R.id.date_tv);

        show_swt = (Switch) findViewById(R.id.show_swt);
    }


}

