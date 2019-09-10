package com.example.myapplication.Model;

import java.io.Serializable;
import java.util.Map;

public class DiaryContent implements Serializable {

    public String title;
    public String content;
    public String user_id;
    public String uid;
    public String timestamp, select_timestamp;
    public int fav_count;
    public Map<String, Boolean> favorites;


    public static class Comment{
        public String user_id;
        public String uid;
        public String comment;

    }

    @Override
    public String toString(){
        return "uid =" + uid + ", user_id = " + user_id;
    }



//    public DiaryContent(){
//
//    }
//
//    public DiaryContent(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }

//    public String getUserId() { return user_id;}
//
//    public void setUser_id(String user_id) { this.user_id = user_id;}
//
//    public String getUid(){return  uid;}
//
//    public void setUid(String uid){ this.uid = uid;}
//
//    public Long getTimestamp(){ return timestamp;}
//
//    public void setTimestamp(Long timestamp){this.timestamp = timestamp;}
//
//    public Long getSelect_timestamp(){return select_timestamp;}
//
//    public void setSelect_timestamp(Long select_timestamp){this.select_timestamp = select_timestamp;}
//
//    public int getFav_count(){return fav_count;}
//
//    public void setFav_count(int fav_count){this.fav_count = fav_count;}
//
//    public Map<String, Boolean> getFavorites(){return favorites;}
//
//    public void setFavorites(Map<String,Boolean> favorites){this.favorites = favorites;}


//    @Override
//    public String toString() {
//        return "DiaryContent{" +
//                "title='" + title + '\'' +
//                ", content='" + content + '\'' +
//                '}';
//    }
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

//, String user_id, String uid, Long timestamp, Long select_timestamp, int fav_count, Map<String, Boolean> favorites
// this.user_id = user_id;
//        this.uid = uid;
//        this.timestamp = timestamp;
//        this.select_timestamp = select_timestamp;
//        this.fav_count = fav_count;
//        this.favorites = favorites;