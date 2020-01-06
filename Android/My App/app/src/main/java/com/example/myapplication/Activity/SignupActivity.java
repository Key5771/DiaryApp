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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_new_email, edit_new_password, edit_new_nickname, edit_new_password_check;
    private Button buttonSignup;
    private TextView textViewMessage;
    private ProgressDialog progressDialog;

    public String nickname, email, password;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        init();
        buttonSignup.setOnClickListener(this);



    }



    private void registerUser(){
        nickname = edit_new_nickname.getText().toString().trim();
        email = edit_new_email.getText().toString().trim();
        password = edit_new_password.getText().toString().trim();
        String password_check = edit_new_password_check.getText().toString().trim();

        if(TextUtils.isEmpty(nickname)){
            Toast.makeText(this, "닉네임을 입력해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email을 입력해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"비밀번호를 입력해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password_check)){
            Toast.makeText(this, "비밀번호를 한번 더 입력해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password_check.equals(password)){
            Toast.makeText(this, "비밀번호를 확인해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }


        progressDialog.setMessage("등록중입니다. 잠시만 기다려주세요!");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            storeUser();
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

    private void storeUser(){

        FirebaseUser user = firebaseAuth.getCurrentUser();

        nickname = edit_new_nickname.getText().toString().trim();
        email = edit_new_email.getText().toString().trim();

        Map<String, String> user_info = new HashMap<>();
        user_info.put("name",nickname);
        user_info.put("Email",email);

        firebaseFirestore.collection("User").document()
                .set(user_info)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void documentReference) {
                        Toast.makeText(SignupActivity.this, "가입되었습니다!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error = e.getMessage();
                        Toast.makeText(SignupActivity.this,"Error :" + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View view){
        if(view == buttonSignup) {
            registerUser();
        }
    }



    public void init(){
        edit_new_email = (EditText) findViewById(R.id.edit_new_email);
        edit_new_nickname = (EditText) findViewById(R.id.edit_new_nickname);
        edit_new_password = (EditText) findViewById(R.id.edit_new_password);
        edit_new_password_check = (EditText) findViewById(R.id.edit_new_password_check);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        textViewMessage = (TextView) findViewById(R.id.textViewMessage);
        progressDialog = new ProgressDialog(this);
    }
}
