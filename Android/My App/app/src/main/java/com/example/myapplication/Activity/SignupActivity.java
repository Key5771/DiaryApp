package com.example.myapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_new_email, edit_new_password;
    private Button buttonSignup;
    private TextView textViewMessage;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        edit_new_email = (EditText) findViewById(R.id.edit_new_email);
        edit_new_password = (EditText) findViewById(R.id.edit_new_password);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        textViewMessage = (TextView) findViewById(R.id.textViewMessage);
        progressDialog = new ProgressDialog(this);

        buttonSignup.setOnClickListener(this);
    }

    private void registerUser(){
        String email = edit_new_email.getText().toString().trim();
        String password = edit_new_password.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email을 입력해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"비밀번호를 입력해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("등록중입니다. 잠시만 기다려주세요!");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else{
                            textViewMessage.setText("에러유형\n -이미 등록된 이메일 \n -암호 최소 6자리 이상\n -서버 에러");
                            Toast.makeText(SignupActivity.this, "등록 에러!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View view){
        if(view == buttonSignup) {
            registerUser();
        }
    }
}
