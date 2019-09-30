package com.example.myapplication.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myapplication.Fragment.BestDiaryFragment;
import com.example.myapplication.Fragment.PrivateDateFirstFragment;
import com.example.myapplication.Fragment.PrivateSelectFirstFragment;
import com.example.myapplication.Fragment.PrivateTodoFragment;
import com.example.myapplication.Fragment.PublicDateFirstFragment;
import com.example.myapplication.Fragment.PublicSelectFirstFragment;

public class TabPagerAdapter2 extends FragmentStatePagerAdapter {
    private int tabCount;

    public TabPagerAdapter2(FragmentManager fm, int tabCount){
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                BestDiaryFragment bestDiaryFragment = new BestDiaryFragment();
                return bestDiaryFragment;
            case 1:
                PublicSelectFirstFragment publicSelectFirstFragment = new PublicSelectFirstFragment();
                return publicSelectFirstFragment;
            case 2:
                PublicDateFirstFragment publicDateFirstFragment = new PublicDateFirstFragment();
                return publicDateFirstFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount(){
        return  tabCount;
    }
}
