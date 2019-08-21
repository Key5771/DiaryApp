package com.example.myapplication.Database;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class FirebasePost {
    public String id;
    public String name;
    public String title;
    public String content;


    public FirebasePost(){}

    public FirebasePost(String id, String name, String title, String content){
        this.id = id;
        this.name = name;
        this.title = title;
        this.content = content;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("title", title);
        result.put("content", content);
        return result;
    }
}
