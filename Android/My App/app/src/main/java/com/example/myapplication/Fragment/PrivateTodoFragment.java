package com.example.myapplication.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Activity.PrivateDetailActivity;
import com.example.myapplication.Adapter.TodoAdapter;
import com.example.myapplication.Model.TodoContent;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PrivateTodoFragment extends Fragment {

    private RecyclerView mTodoList;
    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView.LayoutManager layoutManager;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    List<TodoContent> todoContentList;
    private Map<String, Object> contentMap;
    private TodoAdapter todoAdapter;

    private GestureDetector gestureDetector;

    public PrivateTodoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_private_todo, container, false);

        init(view);
        read_todo();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                read_todo();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

//        delete_todo();
        delete_TODO();


        return view;

    }

    private void init(View view){
        mTodoList = (RecyclerView) view.findViewById(R.id.todo_list);
        layoutManager = new LinearLayoutManager(getActivity());
        mTodoList.setLayoutManager(layoutManager);
        mTodoList.addItemDecoration(new DividerItemDecoration(view.getContext(),1));
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout5);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange_inactive);
    }

    private void read_todo(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        CollectionReference collectionReference = firebaseFirestore.collection("Todo");
        collectionReference.whereEqualTo("user id",user.getEmail()).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        QuerySnapshot documentSnapshots = task.getResult();
                        todoContentList = new ArrayList<>();
                        contentMap = new HashMap<>();

                        for(QueryDocumentSnapshot document : documentSnapshots){
                            TodoContent todoData = new TodoContent();
                            contentMap = document.getData();
                            todoData.id = (String) contentMap.getOrDefault("id","id");
                            todoData.user_id = (String) contentMap.getOrDefault("user id","유저");
                            todoData.todo_content = (String) contentMap.getOrDefault("todo","할일");
                            todoData.select_timestamp = ((Timestamp)contentMap.getOrDefault("timestamp", "날짜")).toDate();

                            todoContentList.add(todoData);

                            Collections.sort(todoContentList, new Comparator<TodoContent>() {
                                @Override
                                public int compare(TodoContent o1, TodoContent o2) {
                                    return o1.select_timestamp.compareTo(o2.select_timestamp);
                                }
                            });
                        }
                        todoAdapter = new TodoAdapter(todoContentList);
                        mTodoList.setAdapter(todoAdapter);
                    } else{
                        Log.d("TODO Fragment","get failed with ", task.getException());
                    }
                });
    }

    private void delete_todo(){
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                firebaseFirestore.collection("Content")
                        .document(PrivateTodoFragment.this.todoContentList.get(position).id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(),"삭제되었습니다",Toast.LENGTH_SHORT).show();
                                read_todo();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(),"실패", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mTodoList);
    }

    private void delete_TODO(){
        //리스트에서 선택한 일기 보내주기
        mTodoList.addOnItemTouchListener(new DiaryFragment.RecyclerTouchListener(getActivity().getApplicationContext(), mTodoList, new DiaryFragment.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            //일기 삭제하기
            @Override
            public void onLongClick(View view, int position) {

                System.out.println("@#@#@@#$%@#$%@#%#$%%$^#$%@%#^@#$^@#$"+PrivateTodoFragment.this.todoContentList.get(position));

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("삭제하겠습니까?");
                alert.setMessage("삭제된 할일은 복구할 수 없습니다.");
                alert.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseFirestore.collection("Todo")
                                .document(PrivateTodoFragment.this.todoContentList.get(position).id)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(),"삭제되었습니다",Toast.LENGTH_SHORT).show();
                                    read_todo();}
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(),"실패", Toast.LENGTH_SHORT).show();
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
