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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activity.AddDiaryActivity;
import com.example.myapplication.Adapter.DiaryAdapter;
import com.example.myapplication.Adapter.TitleAdapter;
import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.Model.TodoContent;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.getDefaultSize;
import static androidx.constraintlayout.widget.Constraints.TAG;


public class CalendarFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private DiaryContent diaryContent;
    private TodoContent todoContent;
    private List<DiaryContent> diaryContentList;

    private DiaryAdapter mDiaryAdapter;
    private TitleAdapter titleAdapter;

    private CalendarView calendarView;
    private TextView titleTextview, contentTextview, timeTextview;
    private Map<String, Object> contentMap;
    private RecyclerView mDayList;
    private GestureDetector gestureDetector;

    private float scale = 1.05f;
    private FloatingActionButton fab, diary_fab, todo_fab;
    private EditText todoText;


    private RecyclerView.LayoutManager layoutManager;
    boolean fab_click = false;
    private int select_year, select_month, select_day;

    @Override
    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_calendar_fragment,container,false);

        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        diary_fab = (FloatingActionButton) view.findViewById(R.id.diary_fab);
        todo_fab = (FloatingActionButton) view.findViewById(R.id.todo_fab);
        mDayList = (RecyclerView) view.findViewById(R.id.day_list);
        todoText = (EditText) view.findViewById(R.id.todo);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        mDayList.addItemDecoration(new DividerItemDecoration(view.getContext(),1));
        layoutManager = new LinearLayoutManager(getContext());
        mDayList.setLayoutManager(layoutManager);

        calendarView.setScaleY(scale);
        calendarView.setClickable(true);
        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        calendarView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view1){
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

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date selectTime = cal.getTime();

                Date day = new Date(year-1900,month,dayOfMonth);


                //선택한 날짜에 저장된 목록 가져오기

//                Date selectDate = selectTime;
//                Date selectDatePlusDay = cal.add(Calendar.DATE,1);

                firebaseFirestore.collection("Content")
                        .whereEqualTo("user id",user.getEmail()).whereEqualTo("select timestamp",day)
                        .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        QuerySnapshot documentSnapshots = task.getResult();
                        diaryContentList = new ArrayList<>();
                        contentMap = new HashMap<>();
                        for(QueryDocumentSnapshot document : documentSnapshots){
                            DiaryContent diaryData = new DiaryContent();
                            contentMap = document.getData();

                            diaryData.title = (String) contentMap.getOrDefault("title","제목");

                            diaryContentList.add(diaryData);
                            Log.i(TAG, contentMap.toString());
                        }
                        titleAdapter = new TitleAdapter(diaryContentList);
                        mDayList.setAdapter(titleAdapter);
                    } else{
                        Log.d(TAG,"get failed with ", task.getException());
                    }
                });
            }
        });


        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(fab_click == false){
                    fab_click = true;
                    showFABMenu();
                }
                else{
                    fab_click = false;
                    closeFABMenu();}
            }
        });

        diary_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date selectDate = new Date(select_year-1900,select_month,select_day);
                if(selectDate.before(currentTime)){
                    Intent transferIntent = new Intent(getActivity(), AddDiaryActivity.class);
                    transferIntent.putExtra("year",select_year);
                    transferIntent.putExtra("month",select_month);
                    transferIntent.putExtra("day",select_day);
                    startActivity(transferIntent);
                }else{
                    Toast.makeText(getActivity(), "오늘 이후의 일기는 쓸 수 없습니다!", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        todo_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_todo(v);
            }
        });

        return view;
    }

    private void input_todo(View view){
        Date selectDate = new Date(select_year-1900,select_month,select_day);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        String todo_date = simpleDateFormat.format(selectDate);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(R.layout.todo_popup);


        alertDialogBuilder.setTitle(todo_date+" 의 할일").setCancelable(false);

        alertDialogBuilder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                todo_save();
            }
        });
        alertDialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),"취소",Toast.LENGTH_LONG).show();
            }
        }); alertDialogBuilder.show();
    }

    private void todo_save(){
        diaryContent = new DiaryContent();
        todoContent = new TodoContent();

        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.YEAR, year);
//        cal.set(Calendar.MONTH, month);
//        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Date selectTime = cal.getTime();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        todoContent.user_id = user.getEmail();
        todoContent.todo_content = todoText.getText().toString(); //null pointer exception
        todoContent.select_timestamp = selectTime;

        Map<String, Object> user_todo = new HashMap<>();

        if (todoContent.todo_content.isEmpty()){
            Toast.makeText(getActivity(),"내용을 입력해주세요!",Toast.LENGTH_SHORT).show();
            return;
        }

        user_todo.put("user id",todoContent.user_id);
        user_todo.put("todo",todoContent.todo_content);
        user_todo.put("timestamp",todoContent.select_timestamp);

        firebaseFirestore.collection("Todo")
                .add(user_todo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(),"저장되었습니다!",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),"저장 실패하셨습니다",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showFABMenu(){
        diary_fab.animate().translationY(-getResources().getDimension(R.dimen.standard_60));
        todo_fab.animate().translationY(-getResources().getDimension(R.dimen.standard_110));
    }

    private void closeFABMenu(){
        diary_fab.animate().translationY(0);
        todo_fab.animate().translationY(0);
    }


}

