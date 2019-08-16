package com.example.myapplication.Model;

public class DiaryContent {

    private int ID;

    private String title;
    private String content;

    public int getID(){ return ID;}
    public void setID(int ID){this.ID = ID;}

    public DiaryContent(){

    }

    public DiaryContent(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
