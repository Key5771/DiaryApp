package com.example.myapplication.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.example.myapplication.Database.FirebasePost;

import java.util.regex.Pattern;

public class SignActivity extends AppCompatActivity {
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("asdf");


    
    private EditText editTextEmail;
    private EditText editTextPassword;
    
    private String email = "";
    private String password = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        
        firebaseAuth = FirebaseAuth.getInstance();
        
        editTextEmail = findViewById(R.id.et)
        
    }
}
