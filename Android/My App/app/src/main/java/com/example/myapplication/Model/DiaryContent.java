package com.example.myapplication.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class DiaryContent implements Serializable {
    public String title;
    public String content;
    public String user_id;
    public String uid;
    public Date timestamp, select_timestamp;
    public int fav_count;
    public Map<String, Boolean> favorites;

    public DiaryContent(){

    }

    public static class Comment{
        public String user_id;
        public String uid;
        public String comment;

    }

    @Override
    public String toString(){
        return "uid =" + uid + ", user_id = " + user_id;
    }

}

/*
    title = 일기 제목
    content = 일기 내용
    uid = 글 올릴때 현 작성자 아이디
    user_id = 글 올린 유저 아이디
    timestamp = 글 올린 시간 (작성시간)
    select_timestamp = 선택한 날짜
    fav_count = 좋아요 카운터
    favorites = 좋아요 한 유저들의 아이디
 */



