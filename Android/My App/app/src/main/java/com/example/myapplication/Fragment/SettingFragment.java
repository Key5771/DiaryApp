package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Activity.LoginActivity;
import com.example.myapplication.Activity.ProfileActivity;
import com.example.myapplication.Activity.SignupActivity;
import com.example.myapplication.Adapter.ListViewAdapter;
import com.example.myapplication.Model.SettingListViewitem;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class SettingFragment extends Fragment implements View.OnClickListener{

    private TextView userEmail_tv, deleteUser_tv;

    private FirebaseAuth firebaseAuth;


    @Override
    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_setting_fragment,container,false);

        userEmail_tv = (TextView) view.findViewById(R.id.userEmail_tv);
        deleteUser_tv = (TextView) view.findViewById(R.id.deleteUser_tv);

        ListView listView = (ListView) view.findViewById(R.id.setting_list);
        ArrayList<SettingListViewitem> data = new ArrayList<>();
        SettingListViewitem myInfo = new SettingListViewitem("개인정보 수정");
        SettingListViewitem alarm = new SettingListViewitem("알림 설정");
        SettingListViewitem appInfo = new SettingListViewitem("어플 정보");
        SettingListViewitem screen = new SettingListViewitem("화면 설정");

        data.add(myInfo);
        data.add(alarm);
        data.add(screen);
        data.add(appInfo);

        ListViewAdapter adapter = new ListViewAdapter(getActivity(), R.layout.setting_item, data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Intent intent1 = new Intent(view.getContext(), ProfileActivity.class);
                    startActivity(intent1);
                }
//                if(position == 1){
//                    Intent intent2 = new Intent(view.getContext(), );
//                }
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null) {
            getActivity().finish();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        userEmail_tv.setText(user.getEmail() + "에서 로그아웃 하기");

        userEmail_tv.setOnClickListener(this);
        deleteUser_tv.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == userEmail_tv) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
            alert_confirm.setMessage("로그아웃 하시겠습니까?").setCancelable(false).setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    firebaseAuth.signOut();
                    Toast.makeText(getActivity(),"로그아웃 되었습니다!",Toast.LENGTH_LONG).show();
                    getActivity().finish();
                    startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
                }
            });
            alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(),"취소되었습니다",Toast.LENGTH_LONG).show();
                }
            });
            alert_confirm.show();
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
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "계정이 삭제되었습니다!", Toast.LENGTH_LONG).show();
                                        getActivity().finish();
                                        startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
                                    } else{
                                        Toast.makeText(getActivity(),"실패하였습니다. 다시 시도해주세요!",Toast.LENGTH_SHORT).show();
                                    }
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
