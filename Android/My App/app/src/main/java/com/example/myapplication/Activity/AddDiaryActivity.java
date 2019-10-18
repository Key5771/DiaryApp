package com.example.myapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.Model.DiaryContent;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddDiaryActivity extends AppCompatActivity {

    private Button save_btn;
    private TextView date_tv;
    private Switch show_swt;
    private ImageView backbtn, imageadd_button;
    private FirebaseFirestore firebaseFirestore;
    private EditText edit_title, edit_content;
    private FirebaseAuth firebaseAuth;
    private StorageReference firebaseStorage;
    private Boolean show_check;

    private DiaryContent diaryContent;
    private ImageView image_preview;
    private Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        init();
        settingDate();

        firebaseStorage = FirebaseStorage.getInstance().getReference();
        diaryContent = new DiaryContent();

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                uploadFile();}

        });

        imageadd_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_select();
            }
        });
    }

    /*
    자료 설정
     */
    public void settingDate(){
        Intent intent = getIntent();
        int year = intent.getIntExtra("year", 1);
        int month = intent.getIntExtra("month", 1);
        int day = intent.getIntExtra("day", 1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        Date selectTime = cal.getTime();
        date_tv.setText(dateFormat.format(selectTime));

    }

    /*
     일기 저장하기
     */
    private void save() {

        diaryContent = new DiaryContent();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        Intent intent = getIntent();
        int year = intent.getIntExtra("year", 1);
        int month = intent.getIntExtra("month", 1);
        int day = intent.getIntExtra("day", 1);

        Date currentTime = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        Date selectTime = cal.getTime();
        Date select_day = new Date(year-1900,month,day);

        diaryContent.user_id = user.getEmail();
        diaryContent.title = edit_title.getText().toString();
        diaryContent.content = edit_content.getText().toString();
        diaryContent.timestamp = currentTime;
        diaryContent.select_timestamp = select_day;

        if (diaryContent.title.isEmpty() || diaryContent.content.isEmpty()) {
            Toast.makeText(AddDiaryActivity.this, "내용을 입력해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }
        show_check = show_swt.isChecked();


        Map<String, Object> user_diary = new HashMap<>();

        user_diary.put("show", show_check);
        user_diary.put("user id", diaryContent.user_id);
        user_diary.put("select timestamp", diaryContent.select_timestamp);
        user_diary.put("timestamp", diaryContent.timestamp);
        user_diary.put("title", diaryContent.title);
        user_diary.put("content", diaryContent.content);

        CollectionReference reference = firebaseFirestore.collection("User");
        reference.whereEqualTo("Email", user.getEmail()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshots = task.getResult();
                for (QueryDocumentSnapshot queryDocumentSnapshot : snapshots) {
                    DiaryContent diaryContent = new DiaryContent();
                    diaryContent.user_name = queryDocumentSnapshot.getData().get("name").toString();
                    user_diary.put("user name", diaryContent.user_name);
                    firebaseFirestore.collection("Content").add(user_diary)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(AddDiaryActivity.this, "저장되었습니다!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    String error = e.getMessage();
                                    Toast.makeText(AddDiaryActivity.this, "Error :" + error, Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            } else {
                Log.d("AddDiaryActivity", "get failed with ", task.getException());
            }
        });

    }

    private void image_select(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            Log.d("Add Diary Activity", "uri:" + String.valueOf(filePath));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image_preview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile() {

        firebaseStorage = FirebaseStorage.getInstance().getReference();

        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            String filename = formatter.format(now) + ".png";
            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://calendary-47258.appspot.com/").child("images/" + filename);
            //올라가거라...
            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }

        private void init() {
            save_btn = (Button) findViewById(R.id.save_button);

            edit_title = (EditText) findViewById(R.id.edit_title);
            edit_content = (EditText) findViewById(R.id.edit_content);

            date_tv = (TextView) findViewById(R.id.date_tv);
            backbtn = (ImageView) findViewById(R.id.backbtn);

            image_preview = (ImageView) findViewById(R.id.imageView4);

            show_swt = (Switch) findViewById(R.id.show_swt);
            if(show_swt.isChecked()==true){
                show_swt.setText("공개");
            } else if(show_swt.isChecked() == false){
                show_swt.setText("비공개");
            }

            imageadd_button = (ImageView) findViewById(R.id.image_plus_btn);
        }

}

