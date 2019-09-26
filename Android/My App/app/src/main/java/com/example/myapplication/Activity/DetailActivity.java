package com.example.myapplication.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    private Intent intent;
    private TextView title_text, content_text, time_text, name_text, selecttime_text;
    private EditText edit_comment;
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
        Date time_st;
        Date date_st;


        DiaryContent diaryContent = (DiaryContent) intent.getSerializableExtra("Content");
        title_st = diaryContent.title;
        content_st = diaryContent.content ;
        time_st = diaryContent.timestamp;
        date_st = diaryContent.select_timestamp;
        name_st = diaryContent.user_name;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");

        title_text.setTypeface(null, Typeface.BOLD);
        title_text.setText(title_st);
        content_text.setText(content_st);
        time_text.setText(dateFormat2.format(time_st));
        selecttime_text.setText(dateFormat.format(date_st)+" 일기");
        name_text.setText(name_st);



        CollectionReference collectionReference = firebaseFirestore.collection("Content");
        collectionReference.document().collection("comment").get();
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
        selecttime_text = (TextView) findViewById(R.id.select_time_tv);

        edit_comment = (EditText) findViewById(R.id.edit_comment);

        left_btn = (ImageView) findViewById(R.id.left_btn);

    }

}
