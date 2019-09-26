package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activity.AddDiaryActivity;
import com.example.myapplication.Adapter.DiaryAdapter;
import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private List<DiaryContent> diaryContentList;
    private GestureDetector gestureDetector;
    private DiaryAdapter mDiaryAdaptor;
    private TextView titleTextview, contentTextview, timeTextview;
    private Map<String, Object> contentMap;
    private RecyclerView mDayList;
    private float scale = 1.05f;

    private EditText todoText;

    private int select_year, select_month, select_day;
    final Context context = getActivity();

    @Override
    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_calendar_fragment,container,false);

        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        mDayList = (RecyclerView) view.findViewById(R.id.day_list);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        calendarView.setScaleY(scale);
        calendarView.setClickable(true);
        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference collectionReference = firebaseFirestore.collection("Content");
                collectionReference.whereEqualTo("user id",user.getEmail()).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        QuerySnapshot documentSnapshots = task.getResult();
                        diaryContentList = new ArrayList<>();
                        contentMap = new HashMap<>();
                        for(QueryDocumentSnapshot document : documentSnapshots){
                            DiaryContent diaryData = new DiaryContent();
                            contentMap = document.getData();

                            diaryData.title = (String) contentMap.get("title");
                            diaryContentList.add(diaryData);
                        }
                        mDiaryAdaptor = new DiaryAdapter(diaryContentList);
                        mDayList.setAdapter(mDiaryAdaptor);
                    } else{
                        Log.d(TAG,"get failed with",task.getException());
                    }
                });

            }
        });
        calendarView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view1){
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View todoView = layoutInflater.inflate(R.layout.todo_popup,null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setView(todoView);

                todoText = (EditText) todoView.findViewById(R.id.todo);
                alertDialogBuilder.setTitle("할일을 입력해주세요!").setCancelable(false);

                alertDialogBuilder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"취소",Toast.LENGTH_LONG).show();
                    }
                }); alertDialogBuilder.show();

//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
               return true;
            }
        });

        Date currentTime = new Date();
        SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MM");
        SimpleDateFormat dateFormatDay = new SimpleDateFormat("dd");
        select_year = Integer.parseInt(dateFormatYear.format(currentTime));
        select_month = Integer.parseInt(dateFormatMonth.format(currentTime)) - 1;
        select_day = Integer.parseInt(dateFormatDay.format(currentTime));


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                //선택한 날짜 넘겨주기
                select_day = dayOfMonth;
                select_month = month;
                select_year = year;

                //선택한 날짜 비교해서 미래일기 막기
                Date selectDate = new Date(select_year,select_month,select_day);
                if(selectDate.compareTo(currentTime) > 0 ){
                    fab.hide();
                    Toast.makeText(view.getContext(),"오늘 이후의 일기는 쓸 수 없습니다!",Toast.LENGTH_SHORT).show();
                } else{
                    fab.show();
                }

                //선택한 날짜에 저장된 목록 가져오기
                CollectionReference collectionReference = firebaseFirestore.collection("Content");
                collectionReference.whereEqualTo("select timestamp",selectDate).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        QuerySnapshot documentSnapshots = task.getResult();
                        diaryContentList = new ArrayList<>();
                        contentMap = new HashMap<>();
                        for(QueryDocumentSnapshot document : documentSnapshots){
                            DiaryContent diaryData = new DiaryContent();
                            contentMap = document.getData();

                            diaryData.title = (String) contentMap.get("title");

                            diaryContentList.add(diaryData);
                        }
                        mDiaryAdaptor = new DiaryAdapter(diaryContentList);
                        mDayList.setAdapter(mDiaryAdaptor);
                    } else{
                        Log.d(TAG,"get failed with ", task.getException());
                    }
                });
            }
        });


        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent transferIntent = new Intent(getActivity(), AddDiaryActivity.class);
                transferIntent.putExtra("year",select_year);
                transferIntent.putExtra("month",select_month);
                transferIntent.putExtra("day",select_day);
                startActivity(transferIntent);
            }
        });

        return view;
    }


}

