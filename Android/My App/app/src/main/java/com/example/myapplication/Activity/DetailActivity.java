package com.example.myapplication.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.Nullable;

public class DetailActivity extends AppCompatActivity {

    private Intent intent;
    private TextView title_text, content_text, name_text;
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





        DiaryContent diaryContent = (DiaryContent) intent.getSerializableExtra("diary");
        title_st = diaryContent.getTitle();
        content_st = diaryContent.getContent() ;

        title_text.setTypeface(null, Typeface.BOLD);
        title_text.setText(title_st);
        content_text.setText(content_st);

    }


    public void onClick(View view){
        if(view == left_btn) {
            finish();
        }
    }

    public void init(){
        title_text = (TextView) findViewById(R.id.detail_diary_title);
        content_text = (TextView) findViewById(R.id.detail_diary_content);
        left_btn = (ImageView) findViewById(R.id.left_btn);
    }

}
