package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.CommentContent;
import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<CommentContent> commentAdapterList;
    private Map<String, CommentAdapter> commentAdapterMap;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView comment_tv, comment_user, comment_timestamp;
        public ViewHolder(View v){
            super(v);
            comment_tv = v.findViewById(R.id.comment_content);
            comment_timestamp = v.findViewById(R.id.comment_timestamp);
            comment_user = v.findViewById(R.id.userID);
        }
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item,parent,false);
        CommentAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public CommentAdapter(){this.commentAdapterList = new LinkedList<>(); }

    public CommentAdapter(List<CommentContent> commentAdapterList){
        this.commentAdapterList = commentAdapterList; }

    public CommentAdapter(Map<String, CommentAdapter> diaryContentMap) {this.commentAdapterMap = diaryContentMap;}

    public void addContent(CommentContent content) {
        commentAdapterList.add(content);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
        String timetv = simpleDateFormat.format(commentAdapterList.get(position).comment_timestamp);

        holder.comment_user.setText(commentAdapterList.get(position).comment_user_id);
        holder.comment_tv.setText(commentAdapterList.get(position).comment_content);
        holder.comment_timestamp.setText(timetv);
    }

    @Override
    public int getItemCount() {
        return commentAdapterList.size();
    }
}

