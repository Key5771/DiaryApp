package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activity.DetailActivity;
import com.example.myapplication.Adapter.DiaryAdapter;
import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

public class DiaryFragment extends Fragment {
    private static final String TAG = "DiaryFragment";

    private RecyclerView mDiaryList;
    private DiaryAdapter mDiaryAdaptor;
    private FirebaseFirestore firebaseFirestore;
    private GestureDetector gestureDetector;
    private FirebaseAuth firebaseAuth;
    private ArrayList arrayList;
    List<DiaryContent> contents;

    @Override
    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_diary_fragment, container, false);

         mDiaryList = view.findViewById(R.id.diary_list);
         mDiaryList.setLayoutManager(new LinearLayoutManager(this.getContext()));
         mDiaryList.addItemDecoration(new DividerItemDecoration(view.getContext(),1));

//         arrayList = new ArrayList<>();


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //일기 선택
        gestureDetector = new GestureDetector(getActivity().getApplicationContext(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e){
                return true;
            }
        });

        //선택한 일기 보내주기
        mDiaryList.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), mDiaryList, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
//                        Dictionary dict = arrayList.get(position);
                        Intent diaryIntent = new Intent(getActivity().getBaseContext(),DetailActivity.class);
                        diaryIntent.putExtra("diary", contents.get(position));
//                        diaryIntent.putExtra("title",contents.get(position).getTitle());
//                        diaryIntent.putExtra("content",contents.get(position).getContent());
                        startActivity(diaryIntent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setTitle("삭제하겠습니까?");
                        alert.setMessage("삭제된 일기는 복구할 수 없습니다.");
                        alert.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseFirestore.collection("diary").document(user.getEmail()).collection(user.getEmail()+"'s diary").document()
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getActivity(),"삭제되었습니다",Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity(),"실패",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
                    }
                }));




        //일기 가져오기
        CollectionReference docRef = firebaseFirestore.collection("diary").document(user.getEmail()).collection(user.getEmail()+"'s diary");
        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                QuerySnapshot documents = task.getResult();
                contents = new ArrayList<>();
                for (QueryDocumentSnapshot document : documents) {
                    Map<String, Object> content1 = document.getData();
                    String title = (String)content1.getOrDefault("title", "제목");
                    String diaryContent = (String)content1.getOrDefault("content", "내용");
                    DiaryContent content = new DiaryContent(title, diaryContent);

                    Log.i(TAG, content.toString());
//                    mDiaryAdaptor.addContent(content);
                    contents.add(content);
                }
                mDiaryAdaptor = new DiaryAdapter(contents);
                mDiaryList.setAdapter(mDiaryAdaptor);

//                mDiaryAdaptor.notifyDataSetChanged();
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
        return view;
    }

    public interface ClickListener{
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private DiaryFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final DiaryFragment.ClickListener clickListener){
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e){
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e){
                    View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if(child != null && clickListener != null){
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e){
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if(child != null && clickListener != null && gestureDetector.onTouchEvent(e)){
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e){
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept){}
    }
}





