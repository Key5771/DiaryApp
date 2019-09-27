package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private TextView userName, userEmail , name_tv, email_tv, image_change;
    private ImageView backbtn, profileImage;
    private DiaryContent diaryContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userEmail.setText(user.getEmail());

        CollectionReference reference = firebaseFirestore.collection("User");
        reference.whereEqualTo("Email",user.getEmail()).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                QuerySnapshot snapshots = task.getResult();
                for (QueryDocumentSnapshot querySnapshot : snapshots){
                    userName.setText(querySnapshot.getData().get("name").toString());
                }
            } else{
                Log.d("ProfileActivity ","get failed with ", task.getException());
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    private void init(){
        userEmail = (TextView) findViewById(R.id.user_email_text);
        userName = (TextView) findViewById(R.id.user_name_text);

        name_tv = (TextView) findViewById(R.id.name);
        name_tv.setTypeface(Typeface.DEFAULT_BOLD);
        email_tv = (TextView) findViewById(R.id.email);
        email_tv.setTypeface(Typeface.DEFAULT_BOLD);

        image_change = (TextView) findViewById(R.id.profile_change);
        image_change.setTypeface(Typeface.DEFAULT_BOLD);

        backbtn = (ImageView) findViewById(R.id.back_button);
        profileImage = (ImageView) findViewById(R.id.profile_image);
    }
}
