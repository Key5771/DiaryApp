package com.example.myapplication.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.myapplication.Activity.DetailActivity;
import com.example.myapplication.Adapter.DiaryAdapter;
import com.example.myapplication.Adapter.PublicAdapter;
import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class PublicDateFirstFragment extends Fragment {

    private RecyclerView pDateList;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    List<DiaryContent> diaryContentList;
    private GestureDetector gestureDetector;
    private DiaryAdapter mDiaryAdaptor;
    private PublicAdapter publicAdapter;
    private TextView titleTextview, contentTextview, timeTextview;
    private SwipeRefreshLayout swipeRefreshLayout;
    Map<String, Object> contentMap;

    private RadioButton newRadioButton, oldRadioButton;
    private RadioGroup radioGroup;

    RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_public_date_first, container, false);

        init(view);
        read_newDiary();
        select_diary();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(newRadioButton.isChecked()){ read_newDiary();}
                else{read_oldDiary();}
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        radioGroup.check(R.id.pub_radioButton1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.pub_radioButton1:
                        read_newDiary();
                        break;
                    case R.id.pub_radioButton2:
                        read_oldDiary();
                        break;
                }
            }
        });

        return view;
    }

    private void init(View view) {
        pDateList = (RecyclerView) view.findViewById(R.id.pub_date_first_list);
        pDateList.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));

        titleTextview = (TextView) view.findViewById(R.id.diary_item_title);
        contentTextview = (TextView) view.findViewById(R.id.diary_item_content);
        timeTextview = (TextView) view.findViewById(R.id.diary_item_date);

        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        newRadioButton = (RadioButton) view.findViewById(R.id.pub_radioButton1);
        oldRadioButton = (RadioButton) view.findViewById(R.id.pub_radioButton2);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout3);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange_inactive);

        layoutManager = new LinearLayoutManager(getActivity());
        pDateList.setLayoutManager(layoutManager);
    }

    private void read_newDiary() {

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();


        //일기 불러오기
        CollectionReference collectionReference = firebaseFirestore.collection("Content");
        collectionReference.whereEqualTo("show", true).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                QuerySnapshot documentSnapshots = task.getResult();
                diaryContentList = new ArrayList<>();
                contentMap = new HashMap<>();
                for (QueryDocumentSnapshot document : documentSnapshots) {
                    DiaryContent diaryData = new DiaryContent();
                    contentMap = document.getData();

                    diaryData.title = (String) contentMap.getOrDefault("title", "제목");
                    diaryData.content = (String) contentMap.getOrDefault("content", "내용");
                    diaryData.select_timestamp = ((Timestamp)contentMap.getOrDefault("select timestamp",0)).toDate();
                    diaryData.user_name = (String)contentMap.getOrDefault("user name","이름");
                    diaryData.timestamp = ((Timestamp) contentMap.getOrDefault("timestamp", 0)).toDate();
                    diaryData.user_id = (String) contentMap.get("user id");

                    diaryContentList.add(diaryData);

                    //최신순 정렬
                    Collections.sort(diaryContentList, new Comparator<DiaryContent>() {
                        @Override
                        public int compare(DiaryContent o1, DiaryContent o2) {
                            return o2.timestamp.compareTo(o1.timestamp);
                        }
                    });

                }
                publicAdapter = new PublicAdapter(diaryContentList);
                pDateList.setAdapter(publicAdapter);
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

    }

    private void read_oldDiary() {

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();


        //일기 불러오기
        CollectionReference collectionReference = firebaseFirestore.collection("Content");
        collectionReference.whereEqualTo("show", true).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                QuerySnapshot documentSnapshots = task.getResult();
                diaryContentList = new ArrayList<>();
                contentMap = new HashMap<>();
                for (QueryDocumentSnapshot document : documentSnapshots) {
                    DiaryContent diaryData = new DiaryContent();
                    contentMap = document.getData();

                    diaryData.title = (String) contentMap.getOrDefault("title", "제목");
                    diaryData.content = (String) contentMap.getOrDefault("content", "내용");
                    diaryData.select_timestamp = ((Timestamp)contentMap.getOrDefault("select timestamp",0)).toDate();
                    diaryData.user_name = (String)contentMap.getOrDefault("user name","이름");
                    diaryData.timestamp = ((Timestamp) contentMap.getOrDefault("timestamp", 0)).toDate();
                    diaryData.user_id = (String) contentMap.get("user id");

                    diaryContentList.add(diaryData);

                    //오래된순 정렬
                    Collections.sort(diaryContentList, new Comparator<DiaryContent>() {
                        @Override
                        public int compare(DiaryContent o1, DiaryContent o2) {
                            return o1.timestamp.compareTo(o2.timestamp);
                        }
                    });

                }
                publicAdapter = new PublicAdapter(diaryContentList);
                pDateList.setAdapter(publicAdapter);
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

    }

    private void select_diary() {

        //일기 선택
        gestureDetector = new GestureDetector(getActivity().getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        //리스트에서 선택한 일기 보내주기
        pDateList.addOnItemTouchListener(new DiaryFragment.RecyclerTouchListener(getActivity().getApplicationContext(), pDateList, new DiaryFragment.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent diaryIntent = new Intent(getActivity().getBaseContext(), DetailActivity.class);
                diaryIntent.putExtra("Content", diaryContentList.get(position));
                startActivity(diaryIntent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

    }
}