package com.example.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_background);

         try{
             Thread.sleep(5000);
             Intent mainIntent = new Intent(this, LoginActivity.class);
             startActivity(mainIntent);
             finish();
         } catch (InterruptedException e){
             e.printStackTrace();
         }

    }

    @Override
    public void onBackPressed(){

    }

}
