package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.Model.SettingListViewitem;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<SettingListViewitem> data;
    private int layout;

    public ListViewAdapter(Context context, int layout, ArrayList<SettingListViewitem> data){
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }

    @Override
    public int getCount(){ return data.size();}

    @Override
    public String getItem(int position) {return data.get(position).getName();}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView=inflater.inflate(layout,parent,false);
        }

        SettingListViewitem settingListViewitem = data.get(position);

        TextView name = (TextView) convertView.findViewById(R.id.setting_text);
        name.setText(settingListViewitem.getName());

        return convertView;
    }
}
