package com.example.myapplication.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.Nullable;

import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    private Intent intent;
    private TextView title_text, content_text, time_text, name_text;
    private DiaryContent diaryContent;
    private FirebaseFirestore firebaseFirestore;
    private ImageView left_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        firebaseFirestore = FirebaseFirestore.getInstance();

        intent = getIntent();
        init();

        left_btn.setOnClickListener(this::onClick);

        String title_st;
        String content_st;
        String name_st;
        Date date_st;


        DiaryContent diaryContent = (DiaryContent) intent.getSerializableExtra("Content");
        title_st = diaryContent.title;
        content_st = diaryContent.content ;
        date_st = diaryContent.timestamp;

        CollectionReference reference = firebaseFirestore.collection("User");
        reference.whereEqualTo("Email",diaryContent.user_id).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                QuerySnapshot snapshots = task.getResult();
                for(QueryDocumentSnapshot queryDocumentSnapshot : snapshots){
                    name_text.setText(queryDocumentSnapshot.getData().get("name").toString());
                }
            } else{
                Log.d("DetailActivity ","get failed with ", task.getException());
            }
        });


        title_text.setTypeface(null, Typeface.BOLD);
        title_text.setText(title_st);
        content_text.setText(content_st);
        time_text.setText(date_st.toString());

    }


    public void onClick(View view){
        if(view == left_btn) {
            finish();
        }
    }

    public void init(){
        title_text = (TextView) findViewById(R.id.detail_diary_title);
        content_text = (TextView) findViewById(R.id.detail_diary_content);
        time_text = (TextView) findViewById(R.id.time_tv);
        name_text = (TextView) findViewById(R.id.user_name);

        left_btn = (ImageView) findViewById(R.id.left_btn);

    }

}
