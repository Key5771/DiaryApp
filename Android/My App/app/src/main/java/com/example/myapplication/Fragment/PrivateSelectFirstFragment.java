package com.example.myapplication.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
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
import android.widget.Toast;

import com.example.myapplication.Activity.DetailActivity;
import com.example.myapplication.Adapter.DiaryAdapter;
import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class PrivateSelectFirstFragment extends Fragment {

    private RecyclerView mWritingList;
    private FirebaseAuth firebaseAuth;
    private  FirebaseFirestore firebaseFirestore;
    List<DiaryContent> diaryContentList;
    private GestureDetector gestureDetector;
    private DiaryAdapter mDiaryAdaptor;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Map<String, Object> contentMap;

    RecyclerView.LayoutManager layoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_private_select_first, container, false);

        init(view);
        read_diary();
        select_diary();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                read_diary();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void init(View view){
        mWritingList = (RecyclerView) view.findViewById(R.id.writing_first_list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout2);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange_inactive);

        mWritingList.addItemDecoration(new DividerItemDecoration(view.getContext(),1));

        layoutManager = new LinearLayoutManager(getActivity());
        mWritingList.setLayoutManager(layoutManager);
    }

    private void read_diary(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //일기 불러오기
        CollectionReference collectionReference = firebaseFirestore.collection("Content");
        collectionReference.whereEqualTo("user id",user.getEmail()).get().addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                QuerySnapshot documentSnapshots = task.getResult();
                diaryContentList = new ArrayList<>();
                contentMap = new HashMap<>();
                for(QueryDocumentSnapshot document : documentSnapshots) {
                    DiaryContent diaryData = new DiaryContent();
                    contentMap = document.getData();

                    diaryData.id = (String) document.getId();
                    diaryData.title = (String) contentMap.getOrDefault("title","제목");
                    diaryData.content = (String) contentMap.getOrDefault("content","내용");
                    diaryData.timestamp = ((Timestamp)contentMap.getOrDefault("timestamp",0)).toDate();
                    diaryData.select_timestamp = ((Timestamp)contentMap.getOrDefault("select timestamp",0)).toDate();
                    diaryData.user_name = (String)contentMap.getOrDefault("user name","이름");
                    diaryData.user_id = (String) contentMap.get("user id");

                    diaryContentList.add(diaryData);
                    Log.i(Constraints.TAG, contentMap.toString());

                    //정렬
                    Collections.sort(diaryContentList,((o1, o2) -> {
                        o2.timestamp.compareTo(diaryData.timestamp);
                        return 0;
                    }));

                }
                mDiaryAdaptor = new DiaryAdapter(diaryContentList);
                mWritingList.setAdapter(mDiaryAdaptor);
            } else{
                Log.d(Constraints.TAG, "get failed with ", task.getException());
            }
        });
    }

    private void select_diary(){

        //일기 선택
        gestureDetector = new GestureDetector(getActivity().getApplicationContext(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e){
                return true;
            }
        });

        //선택한 일기 보내주기
        mWritingList.addOnItemTouchListener(new DiaryFragment.RecyclerTouchListener(getActivity().getApplicationContext(), mWritingList, new DiaryFragment.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent diaryIntent = new Intent(getActivity().getBaseContext(), DetailActivity.class);
                diaryIntent.putExtra("Content", diaryContentList.get(position));
                startActivity(diaryIntent);
            }

            @Override
            public void onLongClick(View view, int position) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("삭제하겠습니까?");
                alert.setMessage("삭제된 일기는 복구할 수 없습니다.");
                alert.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseFirestore.collection("Content")
                                .document(PrivateSelectFirstFragment.this.diaryContentList.get(position).id)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(),"삭제되었습니다",Toast.LENGTH_SHORT).show();
                                        read_diary();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(),"실패",Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
            }
        }));

    }
}
