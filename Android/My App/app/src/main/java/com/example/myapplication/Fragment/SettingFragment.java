package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Activity.LoginActivity;
import com.example.myapplication.Activity.SignupActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SettingFragment extends Fragment implements View.OnClickListener{

    private TextView userEmail_tv, deleteUser_tv;
    private Button logout_btn;

    private FirebaseAuth firebaseAuth;


    @Override
    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_setting_fragment,container,false);

        userEmail_tv = (TextView) view.findViewById(R.id.userEmail_tv);
        deleteUser_tv = (TextView) view.findViewById(R.id.deleteUser_tv);
        logout_btn = (Button) view.findViewById(R.id.logout_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null) {
            getActivity().finish();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        userEmail_tv.setText("반갑습니다!\n" + user.getEmail() + "으로 로그인 하고 있습니다");

        logout_btn.setOnClickListener(this);
        deleteUser_tv.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == logout_btn) {
            firebaseAuth.signOut();
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

        if(v == deleteUser_tv) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
            alert_confirm.setMessage("계정을 삭제할까요?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getActivity(),"계정이 삭제되었습니다!",Toast.LENGTH_LONG).show();
                                    getActivity().finish();
                                    startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
                                }
                            });
                }
            });
            alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(),"취소",Toast.LENGTH_LONG).show();
                }
            });
            alert_confirm.show();
        }
    }
}
