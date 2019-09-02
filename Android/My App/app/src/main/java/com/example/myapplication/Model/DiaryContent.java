package com.example.myapplication.Model;

import java.io.Serializable;

public class DiaryContent implements Serializable {

    private String title;
    private String content;

    public DiaryContent(){

    }

    public DiaryContent(String title, String content ) {
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

    @Override
    public String toString() {
        return "DiaryContent{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
