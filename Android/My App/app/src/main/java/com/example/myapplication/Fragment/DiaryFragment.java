package com.example.myapplication.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.DiaryAdapter;
import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class DiaryFragment extends Fragment {
    private static final String TAG = "DiaryFragment";

    private RecyclerView mDiaryList;
    private DiaryAdapter mDiaryAdaptor;
    private FirebaseFirestore firebaseFirestore;


    @Override
    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_diary_fragment, container, false);

         mDiaryList = view.findViewById(R.id.diary_list);
         mDiaryList.setLayoutManager(new LinearLayoutManager(this.getContext()));
         mDiaryAdaptor = new DiaryAdapter();
         mDiaryList.setAdapter(mDiaryAdaptor);
        firebaseFirestore = FirebaseFirestore.getInstance();

        CollectionReference docRef = firebaseFirestore.collection("diarys");
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    QuerySnapshot documents = task.getResult();

                    for (QueryDocumentSnapshot document : documents) {
                        Map<String, Object> content1 = document.getData();
                        String title = (String)content1.getOrDefault("title", "제목");
                        String diaryContent = (String)content1.getOrDefault("content", "");
                        DiaryContent content = new DiaryContent(title, diaryContent);

                        Log.i(TAG, content.toString());
                        mDiaryAdaptor.addContent(content);
                    }

                    mDiaryAdaptor.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return view;
    }
}





