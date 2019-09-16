package com.example.myapplication.Adapter;


import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragment.DiaryFragment;


public class PagerAdapter {

    private int tabCount;
    private DiaryFragment diaryFragment = new DiaryFragment();
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
