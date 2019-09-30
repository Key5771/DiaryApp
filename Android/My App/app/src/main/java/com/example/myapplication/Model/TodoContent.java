package com.example.myapplication.Model;

import java.io.Serializable;
import java.util.Date;

public class TodoContent implements Serializable {
    public String id;
    public String todo_content;
    public String user_id;
    public Date select_timestamp;

    public TodoContent(){}

    @Override
    public String toString(){return "todo content : "+todo_content + ", user_id = " + user_id;}
}
