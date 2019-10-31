package com.example.myapplication.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PublicAdapter extends RecyclerView.Adapter<PublicAdapter.ViewHolder> {

    private List<DiaryContent> diaryContentList;
    private Map<String, DiaryContent> diaryMap;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titleTextView;
        public TextView contentTextView;
        public TextView timeTextView;
        public TextView nameTextView;
        public ViewHolder(View v) {
            super(v);
            titleTextView = v.findViewById(R.id.pub_item_title);
            contentTextView = v.findViewById(R.id.pub_item_content);
            timeTextView = v.findViewById(R.id.pub_item_date);
            nameTextView = v.findViewById(R.id.pub_item_name);
        }
    }

    @Override
    public PublicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.public_list_item, parent, false);
        PublicAdapter.ViewHolder vh = new ViewHolder(view);
        return vh;
    }


    public PublicAdapter(){
        this.diaryContentList = new LinkedList<>();
    }

    public PublicAdapter(List<DiaryContent> diaryContentList) {
        this.diaryContentList = diaryContentList;
    }

    public PublicAdapter(Map<String, DiaryContent> diaryMap) {
        this.diaryMap = diaryMap;
    }

    public void addContent(DiaryContent content) {
        diaryContentList.add(content);
    }

    @Override
    public void onBindViewHolder(PublicAdapter.ViewHolder holder, int position) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        String timetv = simpleDateFormat.format(diaryContentList.get(position).timestamp);

        holder.titleTextView.setText(diaryContentList.get(position).title);
        holder.contentTextView.setText(diaryContentList.get(position).content);
        holder.timeTextView.setText(timetv);
        holder.nameTextView.setText(diaryContentList.get(position).user_id);

    }

    @Override
    public int getItemCount() {
        return diaryContentList.size();
    }
}
