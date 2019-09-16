package com.example.myapplication.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myapplication.Fragment.PrivateDateFirstFragment;
import com.example.myapplication.Fragment.PrivateSelectFirstFragment;
import com.example.myapplication.Fragment.PrivateTodoFragment;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount){
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                PrivateDateFirstFragment privateDateFirstFragment = new PrivateDateFirstFragment();
                return privateDateFirstFragment;
            case 1:
                PrivateSelectFirstFragment privateSelectFirstFragment = new PrivateSelectFirstFragment();
                return privateSelectFirstFragment;
            case 2:
                PrivateTodoFragment privateTodoFragment = new PrivateTodoFragment();
                return privateTodoFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount(){
        return  tabCount;
    }
}
