package com.example.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends Activity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
         firebaseAuth = FirebaseAuth.getInstance();
         // 자동로그인
         if(firebaseAuth.getCurrentUser() != null){
             startActivity(new Intent(this, MainActivity.class));
         } else {
             Intent mainIntent = new Intent(this, LoginActivity.class);
             startActivity(mainIntent);
         }
        finish();
    }

    @Override
    public void onBackPressed(){

    }

}
