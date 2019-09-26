package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kotlin.jvm.internal.MagicApiIntrinsics;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.ViewHolder> {
    private List<DiaryContent> diaryContentList;
    private Map<String, DiaryContent> diaryContentMap;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title_tv;
        public ViewHolder(View v){
            super(v);
            title_tv = v.findViewById(R.id.title_item);
        }
    }

    @Override
    public TitleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.title_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public TitleAdapter(){this.diaryContentList = new LinkedList<>();
    }

    public TitleAdapter(List<DiaryContent> diaryContentList){
        this.diaryContentList = diaryContentList;
    }

    public TitleAdapter(Map<String, DiaryContent> diaryContentMap) {this.diaryContentMap = diaryContentMap;}


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title_tv.setText(diaryContentList.get(position).title);
    }

    @Override
    public int getItemCount() {
        return diaryContentList.size();
    }
}
