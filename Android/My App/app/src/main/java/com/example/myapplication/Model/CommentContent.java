package com.example.myapplication.Model;

import java.io.Serializable;
import java.util.Date;

public class CommentContent implements Serializable {

    public String id;
    public String comment_content;
    public String comment_user_id;
    public String comment_user_name;
    public Date comment_timestamp;

    public CommentContent(){

    }

}
