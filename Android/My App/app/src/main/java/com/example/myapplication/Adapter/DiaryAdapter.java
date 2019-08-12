package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class DiaryAdapter extends BaseAdapter {
    private List<DiaryContent> diaryContentList;

    public DiaryAdapter(){
        diaryContentList = new ArrayList<DiaryContent>(3);
        diaryContentList.add(new DiaryContent("가", "1"));
        diaryContentList.add(new DiaryContent("나", "2"));
        diaryContentList.add(new DiaryContent("다", "3"));
    }

    public DiaryAdapter(List<DiaryContent> diaryContentList) {
        this.diaryContentList = diaryContentList;
    }

    @Override
    public int getCount() {
        return diaryContentList.size();
    }

    @Override
    public Object getItem(int position) {
        return diaryContentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(convertView == null)
            convertView = inflater.inflate(R.layout.diary_list_item, parent, false);

        TextView title = convertView.findViewById(R.id.diary_item_title);
        title.setText(diaryContentList.get(position).getTitle());
        TextView content = convertView.findViewById(R.id.diary_item_content);
        content.setText(diaryContentList.get(position).getContent());
        return convertView;
    }
}
