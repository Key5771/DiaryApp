package com.example.myapplication.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Constraints;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.CommentAdapter;
import com.example.myapplication.Model.CommentContent;
import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private Intent intent;
    private TextView title_text, content_text, time_text, name_text, selecttime_text, likenum_text;
    private EditText edit_comment;
    private RecyclerView comment_view;
    private CommentAdapter commentAdapter;
    private CommentContent commentContent;
    private ImageView left_btn, like_btn ,comment_btn, more_btn;
    private Map<String, Object> contentMap;
    List<CommentContent> commentContentList;
    RecyclerView.LayoutManager layoutManager;
    private GestureDetector gestureDetector;
    private RecyclerView.OnItemTouchListener itemTouchListener;

    private String docID;
    boolean click = false;
    private int likeCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        commentContent = new CommentContent();

        intent = getIntent();
        init();

        left_btn.setOnClickListener(this::onClick);
        more_btn.setOnClickListener(this::onClick);
        docID = intent.getStringExtra("id");

        String title_st;
        String content_st;
        String name_st, id_st;
        Date time_st;
        Date date_st;

        DiaryContent diaryContent = (DiaryContent) intent.getSerializableExtra("Content");

        title_st = diaryContent.title;
        content_st = diaryContent.content ;
        time_st = diaryContent.timestamp;
        date_st = diaryContent.select_timestamp;
        name_st = diaryContent.user_name;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");

        title_text.setTypeface(null, Typeface.BOLD);
        title_text.setText(title_st);
        content_text.setText(content_st);
        time_text.setText(dateFormat2.format(time_st));
        selecttime_text.setText(dateFormat.format(date_st)+" 일기");
        name_text.setText(name_st);
        name_text.setTypeface(null,Typeface.BOLD);

        like_Count();

        //댓글 저장하기
        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            @Nullable
            public void onClick(View v) {
                save_comment();
            }
        });

        //좋아요 버튼
        firebaseFirestore.collection("Content").document(docID).collection("Favorite")
                .whereEqualTo("favUserID", user.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.out.println("Error : " + e);
                    return;
                }

                if (queryDocumentSnapshots.getDocuments().isEmpty() == true) {
                    click = false;
                    like_btn.setImageResource(R.drawable.heart);
                } else {
                    click = true;
                    like_btn.setImageResource(R.drawable.likefull);
                }
            }
        });



        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like_Count();
                if(click == false){
                    //좋아요 버튼눌렀을때 로그인 유저 저장하기
                    click = true;
                    Map<String, Object> fav = new HashMap<>();
                    fav.put("favUserID",user.getEmail());

                    DocumentReference documentReference = firebaseFirestore.collection("Content").document(docID);
                    System.out.println("aaaaaaaaaaaaa" + docID);

                    documentReference.collection("Favorite")
                            .add(fav)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if(task.isSuccessful()){
                                        like_btn.setImageResource(R.drawable.likefull);
                                    } else{
                                        Toast.makeText(DetailActivity.this,"오류",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                } else {
                    //좋아요 버튼 또 눌렀을 때 저장되어있는 유저 삭제하기
                    click = false;

                   CollectionReference collectionReference = firebaseFirestore.collection("Content").document(docID).collection("Favorite");
                   Query query = collectionReference.whereEqualTo("favUserID", user.getEmail());
                   query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                           if (task.isSuccessful()) {
                               for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                   collectionReference.document(documentSnapshot.getId()).delete();
                                   like_btn.setImageResource(R.drawable.heart);
                               }
                           } else {
                               System.out.println("cccccccc");
                           }
                       }
                   });
                }
            }
        });

        //설정 버튼
        more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOptionsMenu();
            }
        });
        read_comment();
        del_comment();
    }

    //댓글 저장하기
    private void save_comment(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        String comment = edit_comment.getText().toString();
        Date commentDate = Calendar.getInstance().getTime();

        commentContent.comment_user_id = user.getEmail();
        commentContent.comment_timestamp = commentDate;
        commentContent.comment_content = comment;

        CollectionReference collectionReference = firebaseFirestore.collection("Content").document(docID).collection("Comment");
        Map<String, Object> com = new HashMap<>();
        com.put("user id",commentContent.comment_user_id);
        com.put("content",commentContent.comment_content);
        com.put("date",commentContent.comment_timestamp);

        collectionReference.add(com).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
//                    System.out.println("BBBBBBBBBBBBBBBB" + comment);
                    edit_comment.setText("");
                    inputMethodManager.hideSoftInputFromWindow(edit_comment.getWindowToken(),0);
                    read_comment();
                }
                else {
                    Log.d("DetailActivity","comment save error");
                }
            }
        });
    }

    //좋아요 개수 불러오기
    private void like_Count(){
        DiaryContent diaryData = new DiaryContent();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore.collection("Content").document(docID).collection("Favorite")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    likeCount = 0;
                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        likeCount ++;
                        diaryData.like_num = likeCount;
                        likenum_text.setText(String.valueOf(diaryData.like_num));}
                }else {
                    Log.d("DetailActivity", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    //댓글 불러오기
    private void read_comment(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        CollectionReference collectionReference = firebaseFirestore.collection("Content").document(docID).collection("Comment");
        collectionReference.get().addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                QuerySnapshot documentSnapshots = task.getResult();
                commentContentList = new ArrayList<>();
                contentMap = new HashMap<>();
                for(QueryDocumentSnapshot document : documentSnapshots) {
                    CommentContent commentContent = new CommentContent();
                    contentMap = document.getData();

//                    commentContent.id = (String) document.getId();
                    commentContent.comment_content = (String) contentMap.getOrDefault("content","내용");
                    commentContent.comment_timestamp = ((Timestamp)contentMap.getOrDefault("date",0)).toDate();
                    commentContent.comment_user_id = (String) contentMap.get("user id");

//                    Log.d("DetailActivity","!!!!!!!"+contentMap);
                    commentContentList.add(commentContent);

                    //최신순 정렬
                    Collections.sort(commentContentList, new Comparator<CommentContent>() {
                        @Override
                        public int compare(CommentContent o1, CommentContent o2) {
                            return o2.comment_timestamp.compareTo(o1.comment_timestamp);
                        }
                    });

                }
                commentAdapter = new CommentAdapter(commentContentList);
                comment_view.setAdapter(commentAdapter);
            } else{
                Log.d(Constraints.TAG, "get failed with ", task.getException());
            }
        });
    }

    //댓글 선택 & 삭제
    private void del_comment(){
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e){
                return true;
            }
        });

        comment_view.addOnItemTouchListener(new DetailActivity.RecyclerTouchListener(this, comment_view, new DetailActivity.ClickListener(){
            @Override
            public void onClick(View view, int position){
                Toast.makeText(DetailActivity.this,"대댓글 구현중...",Toast.LENGTH_SHORT).show();
            }

            //댓글 삭제
            @Override
            public void onLongClick(View view, int position){

                AlertDialog.Builder alert = new AlertDialog.Builder(DetailActivity.this);
                alert.setTitle("삭제하겠습니까?");
                alert.setMessage("삭제된 일기는 복구할 수 없습니다.");
                alert.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseFirestore.collection("Content").document(docID).collection("Comment")
                                .document(DetailActivity.this.commentContentList.get(position).id)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(DetailActivity.this,"삭제되었습니다",Toast.LENGTH_SHORT).show();
                                        read_comment();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(DetailActivity.this,"실패", Toast.LENGTH_SHORT).show();
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


    }


    public void onClick(View view){
        if(view == left_btn) { finish(); }
        if(view == more_btn) {
            DiaryContent diaryContent = (DiaryContent) intent.getSerializableExtra("Content");
            FirebaseUser user = firebaseAuth.getCurrentUser();
            PopupMenu popupMenu = new PopupMenu(this,view);
            getMenuInflater().inflate(R.menu.setting_menu,popupMenu.getMenu());

//                MenuInflater inflater = popupMenu.getMenuInflater();
//                Menu menu = popupMenu.getMenu();
//                inflater.inflate(R.menu.setting_menu, menu);
            popupMenu.show();

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.update_menu:

                            break;
                        case R.id.delete_menu:

                            AlertDialog.Builder alert = new AlertDialog.Builder(DetailActivity.this);
                            alert.setTitle("정말 삭제하겠습니까?");
                            alert.setMessage("삭제된 일기는 되돌릴 수 없습니다");
                            alert.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    firebaseFirestore.collection("Content").document(docID).collection("Favorite").document().delete();
                                    firebaseFirestore.collection("Content").document(docID).collection("Comment").document().delete();
                                    firebaseFirestore.collection("Content").document(docID)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(DetailActivity.this, "삭제되었습니다", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(DetailActivity.this, "실패하였습니다", Toast.LENGTH_SHORT).show();
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
                    return false;
                }
            });

//            if(user.equals(diaryContent.user_id)){
//
//            } else{
//                Toast.makeText(DetailActivity.this,"ㅎㅎ",Toast.LENGTH_SHORT).show();
//            }

        }
    }

    public void init(){
        title_text = (TextView) findViewById(R.id.detail_diary_title);
        content_text = (TextView) findViewById(R.id.detail_diary_content);
        time_text = (TextView) findViewById(R.id.time_tv);
        name_text = (TextView) findViewById(R.id.user_name);
        selecttime_text = (TextView) findViewById(R.id.select_time_tv);
        likenum_text = (TextView) findViewById(R.id.like_number);

        edit_comment = (EditText) findViewById(R.id.edit_comment);

        comment_view = (RecyclerView) findViewById(R.id.comment_list);

        left_btn = (ImageView) findViewById(R.id.left_btn);
        like_btn = (ImageView) findViewById(R.id.heart_btn);
        comment_btn = (ImageView) findViewById(R.id.comment_btn);
        more_btn = (ImageView) findViewById(R.id.more_btn);

        comment_view.addItemDecoration(new DividerItemDecoration(this,1));
        layoutManager = new LinearLayoutManager(this);
        comment_view.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.delete_menu:
                Toast.makeText(DetailActivity.this,"삭제버튼 클릭!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.update_menu:
                Toast.makeText(DetailActivity.this,"수정버튼 클릭!",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface ClickListener{
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private DetailActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final DetailActivity.ClickListener clickListener){
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
