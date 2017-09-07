package com.example.megakit_10;

import android.database.Cursor;

/**
 * Created by Kalevych_tech@ukr.net on 05.09.2017.
 * обычный POJO  для хранения данных, которые будут выгружаться в ReyclerView,
 * геттеры-сеттеры, есть конструктор, принимающий курсор, в связи с тем, что Адаптер унаследован
 * от CursorRecyclerViewAdapter
 */

public class Owner {

    int id;
    String firstname;
    String lastname;
    int age;
    String telephon;
    String mail;

    public Owner(int id) {
        this.id = id;
        this.firstname = "null";
        this.lastname = null;
        this.age = 0;
        this.telephon = null;
        this.mail = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getTelephon() {
        return telephon;
    }

    public void setTelephon(String telephon) {
        this.telephon = telephon;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public static Owner fromCursor(Cursor cursor) {


        Owner item = new Owner(cursor.getInt(cursor.getColumnIndex("_id")));
        item.setFirstname(cursor.getString(cursor.getColumnIndex("firstname")));
        item.setLastname(cursor.getString(cursor.getColumnIndex("lastname")));
        item.setAge(cursor.getInt(cursor.getColumnIndex("age")));
        item.setTelephon(cursor.getString(cursor.getColumnIndex("telephone")));
        item.setMail(cursor.getString(cursor.getColumnIndex("mail")));
        return item;


    }


}
