package com.example.myapplication.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class PrivateSelectFirstFragment extends Fragment {

    private RecyclerView mWritingList;
    private FirebaseAuth firebaseAuth;
    private  FirebaseFirestore firebaseFirestore;
    List<DiaryContent> contents;
    private GestureDetector gestureDetector;
    private DiaryAdapter mDiaryAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_private_select_first, container, false);

//        mWritingList = (RecyclerView) view.findViewById(R.id.writing_fist_list);
//        mWritingList.setLayoutManager(new LinearLayoutManager(this.getContext()));
//        mWritingList.addItemDecoration(new DividerItemDecoration(view.getContext(),1));

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

//        select_diary();
        return view;
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
                diaryIntent.putExtra("Content", contents.get(position));
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
                        firebaseFirestore.collection("Content").document()
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(),"삭제되었습니다",Toast.LENGTH_SHORT).show();
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


        //일기 불러오기
        CollectionReference docRef = firebaseFirestore.collection("Content");
        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                QuerySnapshot documents = task.getResult();
                contents = new ArrayList<>();
                for (QueryDocumentSnapshot document : documents) {
                    DiaryContent content = new DiaryContent();
                    Map<String, Object> content1 = document.getData();
                    content.title = (String)content1.getOrDefault("title", "제목");
                    content.content = (String)content1.getOrDefault("content", "내용");

                    Log.i(TAG, content.toString());
                    contents.add(content);
                }
//                mDiaryAdaptor = new DiaryAdapter(contentMap);
                mWritingList.setAdapter(mDiaryAdaptor);
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }



}
