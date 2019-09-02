package com.example.myapplication.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.DiaryAdapter;
import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FeelingFragment extends Fragment {

    private RecyclerView mDiaryList;
    private DiaryAdapter mDiaryAdaptor;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    @Nullable


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.activity_feeling_fragment,container,false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        mDiaryList = view.findViewById(R.id.public_diary_list);
        mDiaryList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mDiaryList.addItemDecoration(new DividerItemDecoration(view.getContext(),1));

        mDiaryAdaptor = new DiaryAdapter();
        mDiaryList.setAdapter(mDiaryAdaptor);

       firebaseFirestore.collection("diary").document(user.getEmail()).collection(user.getEmail()+"'s diary")
               .whereEqualTo("show","공개")
               .get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                           for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                               Map<String, Object> content1 = documentSnapshot.getData();
                               String title = (String)content1.getOrDefault("title", "제목");
                               String diaryContent = (String)content1.getOrDefault("content", "내용");
                               DiaryContent content = new DiaryContent(title, diaryContent);

                               Log.i(TAG, content.toString());
                               mDiaryAdaptor.addContent(content);
                           }
                       } else {
                           Log.d(TAG, "Error getting documents : ", task.getException());
                       }
                   }
               });
        return view;
    }
}
