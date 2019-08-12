package com.example.myapplication.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.Adapter.DiaryAdapter;
import com.example.myapplication.Database.TaskContract;
import com.example.myapplication.Database.TaskDbHelper;
import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DiaryFragment extends Fragment {
    private ListView mDiaryListView;

    @Override
    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_diary_fragment,container,false);
//        mDiaryListView = (ListView) getView().findViewById(R.id.list_diary);

        List<DiaryContent> diaryContentList = new LinkedList<>();

        TaskDbHelper taskDbHelper = new TaskDbHelper(getContext());
        SQLiteDatabase sqLiteDatabase = taskDbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(TaskContract.TaskEntry.TABLE, new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);

        while(cursor.moveToNext()) {
            DiaryContent content = new DiaryContent();
            int index = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            content.setTitle(cursor.getString(1));
            content.setContent("내용이 없습니다");
            diaryContentList.add(content);
        }

        mDiaryListView = view.findViewById(R.id.diary_list);
        mDiaryListView.setAdapter(new DiaryAdapter(diaryContentList));

        return view;
    }


}

