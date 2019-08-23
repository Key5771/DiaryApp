package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edit_userEmail;
    private Button find_btn;
    private TextView textViewMessage;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        edit_userEmail = (EditText) findViewById(R.id.edit_userEmail);
        find_btn = (Button) findViewById(R.id.find_btn);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        find_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if(view == find_btn){
            progressDialog.setMessage("처리중입니다. 잠시만 기다려주세요");
            progressDialog.show();

            String emailAddress = edit_userEmail.getText().toString().trim();
            firebaseAuth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(FindActivity.this, "이메일을 보냈습니다!",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            } else{
                                Toast.makeText(FindActivity.this,"메일 보내기 실패!", Toast.LENGTH_SHORT).show();
                            } progressDialog.dismiss();
                        }
                    });
        }
    }
}
