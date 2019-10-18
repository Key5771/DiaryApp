package com.example.myapplication.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class PrivateDetailActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private Intent intent;
    private TextView title_text, content_text, time_text, selecttime_text;
    private ImageView left_btn, more_btn;
    private Map<String, Object> contentMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_detail);

        init();
        load_diary();

        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init(){
        title_text = (TextView) findViewById(R.id.priv_detail_title);
        content_text = (TextView) findViewById(R.id.priv_detail_content);
        time_text = (TextView) findViewById(R.id.priv_detail_timestamp);
        selecttime_text = (TextView) findViewById(R.id.priv_detail_selectTimestamp);

        left_btn = (ImageView) findViewById(R.id.priv_backbtn);
        more_btn = (ImageView) findViewById(R.id.priv_morebtn);
    }

    private void load_diary(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        intent = getIntent();

        String title_st;
        String content_st;
        Date time_st;
        Date date_st;

        DiaryContent diaryContent = (DiaryContent) intent.getSerializableExtra("Content");

        title_st = diaryContent.title;
        content_st = diaryContent.content ;
        time_st = diaryContent.timestamp;
        date_st = diaryContent.select_timestamp;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yy. MM. dd. E");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");

        title_text.setText(title_st);
        content_text.setText(content_st);
        time_text.setText(dateFormat2.format(time_st));
        selecttime_text.setText(dateFormat.format(date_st));
    }
}

