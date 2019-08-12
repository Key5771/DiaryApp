package com.example.myapplication.Adapter;



import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.myapplication.Fragment.DiaryFragment;
import com.example.myapplication.R;


public class PagerAdapter {

    private int tabCount;
    private DiaryFragment diaryFragment = new DiaryFragment();;
    private FragmentManager fragmentManager;

    public PagerAdapter(FragmentManager fragmentManager, int tabCount) {
        //super(fragmentManager);
        this.tabCount = tabCount;


    }

//    public Fragment getItem(int position) {
//        switch (position){
//            case 0: {
//
//            }
//
}
