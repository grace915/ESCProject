package com.example.escproject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DummyData { //정보들

    public static ArrayList<Contact> contacts = new ArrayList<>();

    static {
      contacts.add(new Contact("이가은", "010-5394-2915","grace915@naver.com"));
      contacts.add(new Contact("이진홍", "010-5384-2915","broad915@naver.com"));
      contacts.add(new Contact("바보", "010-1234-5678","honey@naver.com"));


    }
}
