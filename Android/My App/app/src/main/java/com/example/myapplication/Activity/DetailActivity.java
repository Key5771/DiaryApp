package com.example.myapplication.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private Intent intent;
    private TextView title_text, content_text, time_text, name_text, selecttime_text;
    private EditText edit_comment;
    private DiaryContent diaryContent;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private ImageView left_btn, like_btn ,comment_btn;
    List<DiaryContent> diaryContentList;

    private String docID;
    int i = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        intent = getIntent();
        init();

        left_btn.setOnClickListener(this::onClick);

        String title_st;
        String content_st;
        String name_st;
        Date time_st;
        Date date_st;

        docID = intent.getStringExtra("id");

        DiaryContent diaryContent = (DiaryContent) intent.getSerializableExtra("Content");
        title_st = diaryContent.title;
        content_st = diaryContent.content ;
        time_st = diaryContent.timestamp;
        date_st = diaryContent.select_timestamp;
        name_st = diaryContent.user_id;


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");

        title_text.setTypeface(null, Typeface.BOLD);
        title_text.setText(title_st);
        content_text.setText(content_st);
        time_text.setText(dateFormat2.format(time_st));
        selecttime_text.setText(dateFormat.format(date_st)+" 일기");
        name_text.setText(name_st);

        String comment = edit_comment.getText().toString();

        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!"+comment);
            }
        });



        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 1 - i;
                if(i == 0){
                    //좋아요 버튼 한번 눌렀을 때

                    Map<String, Object> fav = new HashMap<>();
                    fav.put("favUserID",user.getEmail());

                    DocumentReference documentReference = firebaseFirestore.collection("Content")
                            .document(docID);
                    documentReference.collection("Favorite")
                            .add(fav)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if(task.isSuccessful()){
                                        like_btn.setImageResource(R.drawable.likefull);
                                    } else{
                                        Toast.makeText(DetailActivity.this,"오류",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    //좋아요 버튼 한번 더 눌렀을 때

                    Map<String, Object> favUser = new HashMap<>();
                    favUser.put("favUserID", FieldValue.delete());

                    DocumentReference docRef = firebaseFirestore.collection("Content")
                            .document(DetailActivity.this.diaryContent.id);

                    docRef.collection("Favorite").document().update(favUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                like_btn.setImageResource(R.drawable.heart);
                            } else{
                                Toast.makeText(DetailActivity.this,"오류",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }


    public void onClick(View view){
        if(view == left_btn) { finish(); }
    }

    public void init(){
        title_text = (TextView) findViewById(R.id.detail_diary_title);
        content_text = (TextView) findViewById(R.id.detail_diary_content);
        time_text = (TextView) findViewById(R.id.time_tv);
        name_text = (TextView) findViewById(R.id.user_name);
        selecttime_text = (TextView) findViewById(R.id.select_time_tv);

        edit_comment = (EditText) findViewById(R.id.edit_comment);

        left_btn = (ImageView) findViewById(R.id.left_btn);
        like_btn = (ImageView) findViewById(R.id.heart_btn);
        comment_btn = (ImageView) findViewById(R.id.comment_btn);

    }

}
