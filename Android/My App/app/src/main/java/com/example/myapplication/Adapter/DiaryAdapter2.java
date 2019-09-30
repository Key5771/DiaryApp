package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DiaryAdapter2 extends RecyclerView.Adapter<DiaryAdapter2.ViewHolder>{
    private List<DiaryContent> diaryContentList;
    private Map<String, DiaryContent> diaryMap;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView contentTextView;
        public TextView timeTextView;
        public ViewHolder(View v) {
            super(v);
            titleTextView = v.findViewById(R.id.diary_item_title);
            contentTextView = v.findViewById(R.id.diary_item_content);
            timeTextView = v.findViewById(R.id.diary_item_date);
        }
    }

    @Override
    public DiaryAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diary_list_item, parent, false);
        DiaryAdapter2.ViewHolder vh = new DiaryAdapter2.ViewHolder(v);
        return vh;
    }


    public DiaryAdapter2(){
        this.diaryContentList = new LinkedList<>();
    }

    public DiaryAdapter2(List<DiaryContent> diaryContentList) {
        this.diaryContentList = diaryContentList;
    }

    public DiaryAdapter2(Map<String, DiaryContent> diaryMap) {
        this.diaryMap = diaryMap;
    }

    public void addContent(DiaryContent content) {
        diaryContentList.add(content);
    }

    @Override
    public void onBindViewHolder(DiaryAdapter2.ViewHolder holder, int position) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        String timetv = simpleDateFormat.format(diaryContentList.get(position).select_timestamp);

        holder.titleTextView.setText(diaryContentList.get(position).title);
        holder.contentTextView.setText(diaryContentList.get(position).content);
        holder.timeTextView.setText(timetv);
    }

    @Override
    public int getItemCount() {
        return diaryContentList.size();
    }
}
