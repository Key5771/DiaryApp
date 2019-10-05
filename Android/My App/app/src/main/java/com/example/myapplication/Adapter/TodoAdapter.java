package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.TodoContent;
import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private List<TodoContent> todoContentList;


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView todo_content, todo_timestamp;
        public ViewHolder(View v){
            super(v);
            todo_content = v.findViewById(R.id.todo_content);
            todo_timestamp = v.findViewById(R.id.todo_timestamp);
        }
    }

    @NonNull
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    public TodoAdapter(){ this.todoContentList = new LinkedList<>(); }

    public TodoAdapter(List<TodoContent> todoContentList){
        this.todoContentList = todoContentList;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ViewHolder holder, int position) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        String timestamp = simpleDateFormat.format(todoContentList.get(position).select_timestamp);

        holder.todo_content.setText(todoContentList.get(position).todo_content);
        holder.todo_timestamp.setText(timestamp);
    }

    @Override
    public int getItemCount() {
        return todoContentList.size();
    }
}
