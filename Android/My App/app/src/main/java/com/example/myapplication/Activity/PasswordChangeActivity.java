package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class PasswordChangeActivity extends AppCompatActivity {

    private ImageView leftBTN;
    private EditText currentPassword, newPassword, passwordCheck;
    private TextView forgetPassword;
    private Button changeBTN;

    private String cPass, nPass, nPassCheck;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        init();
        getNewPassword();

        firebaseAuth = FirebaseAuth.getInstance();

        leftBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getNewPassword(){

        FirebaseUser user = firebaseAuth.getCurrentUser();

        cPass = currentPassword.getText().toString().trim();
        nPass = newPassword.getText().toString().trim();
        nPassCheck = passwordCheck.getText().toString().trim();

        if(TextUtils.isEmpty(cPass)){
            Toast.makeText(this, "현재 비밀번호를 입력해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(nPass)){
            Toast.makeText(this,"새 비밀번호를 입력해주세요!",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(nPassCheck)){
            Toast.makeText(this, "비밀번호를 한번 더 입력해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!nPass.equals(nPassCheck)){
            Toast.makeText(this,"비밀번호를 확인해주세요!",Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void init(){
        leftBTN = (ImageView) findViewById(R.id.leftButton);

        currentPassword = (EditText) findViewById(R.id.current_password_edit);
        newPassword = (EditText) findViewById(R.id.new_password_edit);
        passwordCheck = (EditText) findViewById(R.id.password_check_edit);

        forgetPassword = (TextView) findViewById(R.id.find_password);
        changeBTN = (Button) findViewById(R.id.passwordChangeBUTTON);
    }
}
