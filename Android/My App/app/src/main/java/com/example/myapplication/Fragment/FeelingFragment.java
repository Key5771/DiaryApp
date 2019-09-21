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
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Activity.DetailActivity;
import com.example.myapplication.Adapter.DiaryAdapter;
import com.example.myapplication.Adapter.TabPagerAdapter;
import com.example.myapplication.Adapter.TabPagerAdapter2;
import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FeelingFragment extends Fragment {

    private RecyclerView mDiaryList;
    private DiaryAdapter mDiaryAdaptor;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mWritingList, mDateList, mTodoList;
    private GestureDetector gestureDetector;
    private FirebaseAuth firebaseAuth;
    private Handler handler;
    List<DiaryContent> contents;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    @Nullable


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_feeling_fragment, container, false);

//탭 레이아웃 설정

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout2);
        tabLayout.addTab(tabLayout.newTab().setText("작성순"));
        tabLayout.addTab(tabLayout.newTab().setText("일기순"));
        tabLayout.addTab(tabLayout.newTab().setText("인기순"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) view.findViewById(R.id.view_pager2);

        TabPagerAdapter2 pagerAdapter2 = new TabPagerAdapter2(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter2);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();


        return view;
    }


//    private void changeView(int index){
//        RecyclerView writing_first = (RecyclerView) getView().findViewById(R.id.writing_fist_list);
//        RecyclerView date_first = (RecyclerView) getView().findViewById(R.id.date_first_list);
//        RecyclerView todo_first = (RecyclerView) getView().findViewById(R.id.todo_list);
//
//        Log.i("ChangeViewTest", String.format("change view index : %d", index));
//
//        switch (index){
//            case 0 :
//                writing_first.setVisibility(View.VISIBLE);
//                date_first.setVisibility(View.INVISIBLE);
//                todo_first.setVisibility(View.INVISIBLE);
//                break;
//            case 1 :
//                writing_first.setVisibility(View.INVISIBLE);
//                date_first.setVisibility(View.VISIBLE);
//                todo_first.setVisibility(View.INVISIBLE);
//                break;
//            case 2 :
//                writing_first.setVisibility(View.INVISIBLE);
//                date_first.setVisibility(View.INVISIBLE);
//                todo_first.setVisibility(View.VISIBLE);
//                break;
//        }
//    }
//        firebaseFirestore = FirebaseFirestore.getInstance();
//        firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//
//        mDiaryList = view.findViewById(R.id.public_diary_list);
//        mDiaryList.setLayoutManager(new LinearLayoutManager(this.getContext()));
//        mDiaryList.addItemDecoration(new DividerItemDecoration(view.getContext(),1));
//
//        mDiaryAdaptor = new DiaryAdapter();
//        mDiaryList.setAdapter(mDiaryAdaptor);
//        DiaryContent diaryContent = new DiaryContent();
//
//       firebaseFirestore.collection("Content")
//               .whereEqualTo("show",true)
//               .get()
//               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                   @Override
//                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                       if (task.isSuccessful()) {
//                           for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                               Map<String, Object> content1 = documentSnapshot.getData();
//                               diaryContent.title = (String)content1.getOrDefault("title", "제목이 없습니다");
//                               diaryContent.content = (String)content1.getOrDefault("content", "내용이 없습니다");
//
//                               mDiaryAdaptor.addContent(diaryContent);
//                           }
//                       } else {
//                           Log.d(TAG, "Error getting documents : ", task.getException());
//                       }
//                   }
//               });
//        return view;
//    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private DiaryFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final DiaryFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}