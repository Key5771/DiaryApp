package com.example.myapplication.Database;


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

}
