package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.Activity.DetailActivity;
import com.example.myapplication.Adapter.DiaryAdapter;
import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DiaryFragment extends Fragment {
    private static final String TAG = "DiaryFragment";

    private RecyclerView mWritingList, mDateList, mTodoList;
    private DiaryAdapter mDiaryAdaptor;
    private FirebaseFirestore firebaseFirestore;
    private GestureDetector gestureDetector;
    private FirebaseAuth firebaseAuth;
    private Handler handler;
    List<DiaryContent> contents;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_diary_fragment, container, false);

        //탭 레이아웃 설정
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                changeView(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


//당겨서 새로고침
//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                select_diary();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
//        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.orange_inactive));

        mWritingList = (RecyclerView) view.findViewById(R.id.writing_fist_list);
        mWritingList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mWritingList.addItemDecoration(new DividerItemDecoration(view.getContext(),1));

        mDateList = (RecyclerView) view.findViewById(R.id.date_first_list);
        mDateList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mDateList.addItemDecoration(new DividerItemDecoration(view.getContext(),1));

        mTodoList = (RecyclerView) view.findViewById(R.id.todo_list);
        mTodoList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mTodoList.addItemDecoration(new DividerItemDecoration(view.getContext(),1));

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();


//로딩 dialog
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("잠시만 기다려주세요..");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
        progressDialog.show();

        select_diary();

        progressDialog.dismiss();

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
        mWritingList.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), mWritingList, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent diaryIntent = new Intent(getActivity().getBaseContext(),DetailActivity.class);
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
                        firebaseFirestore.collection("diarys").document()
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
//                mDiaryAdaptor = new DiaryAdapter(contents);
                mWritingList.setAdapter(mDiaryAdaptor);
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }


    public interface ClickListener{
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private DiaryFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final DiaryFragment.ClickListener clickListener){
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e){
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e){
                    View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if(child != null && clickListener != null){
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e){
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if(child != null && clickListener != null && gestureDetector.onTouchEvent(e)){
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e){
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept){}
    }

    private void changeView(int index){
        RecyclerView writing_first = (RecyclerView) getView().findViewById(R.id.writing_fist_list);
        RecyclerView date_first = (RecyclerView) getView().findViewById(R.id.date_first_list);
        RecyclerView todo_first = (RecyclerView) getView().findViewById(R.id.todo_list);

        Log.i("ChangeViewTest", String.format("change view index : %d", index));

        switch (index){
            case 0 :
                writing_first.setVisibility(View.VISIBLE);
                date_first.setVisibility(View.INVISIBLE);
                todo_first.setVisibility(View.INVISIBLE);
                break;
            case 1 :
                writing_first.setVisibility(View.INVISIBLE);
                date_first.setVisibility(View.VISIBLE);
                todo_first.setVisibility(View.INVISIBLE);
                break;
            case 2 :
                writing_first.setVisibility(View.INVISIBLE);
                date_first.setVisibility(View.INVISIBLE);
                todo_first.setVisibility(View.VISIBLE);
                break;
        }
    }
}





