package com.example.myapplication.Activity;

import android.content.Intent;
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
    private TextView title_text, content_text;
    private DiaryContent diaryContent;
    private Button delete_btn, replace_btn;
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

        String title_st = "";
        String content_st = "";

        Bundle extras = getIntent().getExtras();

        title_st = extras.getString("title");
        content_st = extras.getString("content");

        title_text.setText(title_st);
        content_text.setText(content_st);

//        title_text.setText(diaryContent.getTitle());
//        content_text.setText(diaryContent.getContent());
    }


    public void onClick(View view){
        if(view == left_btn) {
            finish();
        }
    }

    public void init(){
        title_text = (TextView) findViewById(R.id.detail_diary_title);
        content_text = (TextView) findViewById(R.id.detail_diary_content);
        delete_btn = (Button) findViewById(R.id.delete_btn);
        replace_btn = (Button) findViewById(R.id.replace_btn);
        left_btn = (ImageView) findViewById(R.id.left_btn);
    }

}
