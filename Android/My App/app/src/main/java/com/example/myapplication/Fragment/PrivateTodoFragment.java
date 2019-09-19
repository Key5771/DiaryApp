package com.example.myapplication.Fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;


public class PrivateTodoFragment extends Fragment {

    private RecyclerView mTodoList;

    public PrivateTodoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_private_todo, container, false);

//        mTodoList = (RecyclerView) view.findViewById(R.id.todo_list);
//        mTodoList.setLayoutManager(new LinearLayoutManager(this.getContext()));
//        mTodoList.addItemDecoration(new DividerItemDecoration(view.getContext(),1));

        return view;

    }


}
