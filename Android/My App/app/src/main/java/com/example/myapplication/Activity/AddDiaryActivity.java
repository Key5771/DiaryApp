package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class AddDiaryActivity extends AppCompatActivity {

    private Button save_btn;
    private TextView date_tv;
    private Switch show_swt;
    private ImageView backbtn;
    private FirebaseFirestore firebaseFirestore;
    private EditText edit_title, edit_content;
    private FirebaseAuth firebaseAuth;
    private Boolean show_check;

    private DiaryContent diaryContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        init();
        settingDate();

        diaryContent = new DiaryContent();

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(); }

        });
    }

    public void settingDate(){
        Intent intent = getIntent();
        int year = intent.getIntExtra("year", 1);
        int month = intent.getIntExtra("month", 1);
        int day = intent.getIntExtra("day", 1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        Date selectTime = cal.getTime();
        date_tv.setText(dateFormat.format(selectTime));

    }

    private void save() {

        diaryContent = new DiaryContent();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        Intent intent = getIntent();
        int year = intent.getIntExtra("year", 1);
        int month = intent.getIntExtra("month", 1);
        int day = intent.getIntExtra("day", 1);

        Date currentTime = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        Date selectTime = cal.getTime();
        Date select_day = new Date(year-1900,month,day);

        diaryContent.user_id = user.getEmail();
        diaryContent.title = edit_title.getText().toString();
        diaryContent.content = edit_content.getText().toString();
        diaryContent.timestamp = currentTime;
        diaryContent.select_timestamp = select_day;

        Map<String, Object> user_diary = new HashMap<>();

        CollectionReference reference = firebaseFirestore.collection("User");
        reference.whereEqualTo("Email", user.getEmail()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshots = task.getResult();
                for (QueryDocumentSnapshot queryDocumentSnapshot : snapshots) {
                    DiaryContent diaryContent = new DiaryContent();
                    diaryContent.user_name = queryDocumentSnapshot.getData().get("name").toString();
                    user_diary.put("user name", diaryContent.user_name);
                }
            } else {
                Log.d("AddDiaryActivity", "get failed with ", task.getException());
            }
        });



        if (diaryContent.title.isEmpty() || diaryContent.content.isEmpty()) {
            Toast.makeText(AddDiaryActivity.this, "내용을 입력해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        show_check = show_swt.isChecked();

        user_diary.put("show", show_check);
        user_diary.put("user id", diaryContent.user_id);
        user_diary.put("select timestamp", diaryContent.select_timestamp);
        user_diary.put("timestamp", diaryContent.timestamp);
        user_diary.put("title", diaryContent.title);
        user_diary.put("content", diaryContent.content);



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
                        Toast.makeText(AddDiaryActivity.this, "Error :" + error, Toast.LENGTH_SHORT).show();
                    }
                });

    }

        private void init() {
            save_btn = (Button) findViewById(R.id.save_button);

            edit_title = (EditText) findViewById(R.id.edit_title);
            edit_content = (EditText) findViewById(R.id.edit_content);

            date_tv = (TextView) findViewById(R.id.date_tv);
            backbtn = (ImageView) findViewById(R.id.backbtn);
            show_swt = (Switch) findViewById(R.id.show_swt);
        }

}

