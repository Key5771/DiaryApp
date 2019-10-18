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
        tabLayout.addTab(tabLayout.newTab().setText("인기순"));
        tabLayout.addTab(tabLayout.newTab().setText("작성순"));
        tabLayout.addTab(tabLayout.newTab().setText("일기순"));
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





}