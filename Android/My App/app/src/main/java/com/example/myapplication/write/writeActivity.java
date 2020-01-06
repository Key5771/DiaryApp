package com.example.myapplication.write;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class writeActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.save_button)
    Button save_btn;
    @BindView(R.id.edit_title)
    EditText edit_title;
    @BindView(R.id.edit_content)
    EditText edit_content;
    @BindView(R.id.date_tv)
    TextView date_tv;
    @BindView(R.id.backbtn)
    ImageView backbtn;
    @BindView(R.id.show_swt)
    Switch show_swt;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private Boolean show_check;

    private DiaryContent diaryContent;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        ButterKnife.bind(this);
    }


}
