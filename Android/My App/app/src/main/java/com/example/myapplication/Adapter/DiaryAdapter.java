package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;

import org.w3c.dom.Text;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {
    private List<DiaryContent> diaryContentList;
    private Map<String, DiaryContent> diaryMap;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
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
    public DiaryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diary_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    public DiaryAdapter(){
        this.diaryContentList = new LinkedList<>();
    }

    public DiaryAdapter(List<DiaryContent> diaryContentList) {
        this.diaryContentList = diaryContentList;
    }

    public DiaryAdapter(Map<String, DiaryContent> diaryMap) {
        this.diaryMap = diaryMap;
    }

    public void addContent(DiaryContent content) {
        diaryContentList.add(content);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titleTextView.setText(diaryContentList.get(position).title);
        holder.contentTextView.setText(diaryContentList.get(position).content);
        holder.timeTextView.setText(diaryContentList.get(position).timestamp.toString());
    }

    @Override
    public int getItemCount() {
        return diaryContentList.size();
    }
}
