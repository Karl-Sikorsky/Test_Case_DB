package com.example.megakit_10;

import android.database.Cursor;

/**
 * Created by ПОДАРУНКОВИЙ on 05.09.2017.
 */

public class Auto {
int id;
    String number;
    String owner;
    String model;
    int year;
    int price;

    public static Auto fromCursor(Cursor cursor) {


        Auto item = new Auto(cursor.getInt(cursor.getColumnIndex("_id")));
        item.setNumber(cursor.getString(cursor.getColumnIndex("number")));
        item.setOwner(String.valueOf(cursor.getInt(cursor.getColumnIndex("owner"))));
        item.setModel(cursor.getString(cursor.getColumnIndex("model")));
        item.setYear(cursor.getInt(cursor.getColumnIndex("year")));
        item.setPrice(cursor.getInt(cursor.getColumnIndex("price")));
        return item;


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Auto(int id) {
        number = "Number";
        owner = "Owner first last name telephon";
        model = "Model";
        year = 2000;
        price = 1000000;
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Auto(int id, String number, String owner, String model, int year, int price) {

        this.id = id;
        this.number = number;
        this.owner = owner;
        this.model = model;
        this.year = year;
        this.price = price;
    }
}
