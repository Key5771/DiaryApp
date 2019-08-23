package com.example.myapplication.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Adapter.DiaryAdapter;
import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;

public class DiaryFragment extends Fragment {
    private ListView mDiaryListView;
    private DiaryAdapter adapter;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference titleRef = databaseReference.child("title");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_diary_fragment, container, false);
//        mDiaryListView = (ListView) getView().findViewById(R.id.list_diary);

        List<DiaryContent> diaryContentList = new LinkedList<>();
//
//        TaskDbHelper taskDbHelper = new TaskDbHelper(getContext());
//        SQLiteDatabase sqLiteDatabase = taskDbHelper.getWritableDatabase();
//        Cursor cursor = sqLiteDatabase.query(TaskContract.TaskEntry.TABLE, new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_TASK_CONTENT},
//                null, null, null, null, null);
//
//        while (cursor.moveToNext()) {
//            DiaryContent content = new DiaryContent();
//            content.setTitle(cursor.getString(1));
//            content.setContent(cursor.getString(2));
//            diaryContentList.add(content);
//        }
//
        mDiaryListView = view.findViewById(R.id.diary_list);
        mDiaryListView.setAdapter(new DiaryAdapter(diaryContentList));
//
//        mDiaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                int textRes = ((DiaryContent)adapter.getItem(position)).getID();
//
//                Intent intent = new Intent(getActivity(), DetailActivity.class);
//                intent.putExtra("textRes",textRes);
//                intent.putExtra("title", TaskContract.TaskEntry.COL_TASK_TITLE);
//                intent.putExtra("content", TaskContract.TaskEntry.COL_TASK_CONTENT);
//                startActivity(intent);
//
//
//            }
//        });


        return view;
    }


    }





