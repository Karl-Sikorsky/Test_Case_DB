package com.example.megakit_10;

import android.database.Cursor;

/**
 * Created by ПОДАРУНКОВИЙ on 05.09.2017.
 */

public class Car {
int id;
    String number;
    int owner_id;
    String model;
    int year;
    int price;

    public static Car fromCursor(Cursor cursor) {


        Car item = new Car(cursor.getInt(cursor.getColumnIndex("_id")));
        item.setNumber(cursor.getString(cursor.getColumnIndex("number")));
        item.setOwner(cursor.getInt(cursor.getColumnIndex("owner")));
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

    public Car(int id) {
        number = "Number";
        owner_id = 0;
        model = "Model";
        year = 2000;
        price = 1000000;
        this.id = id;
    }

    public int getOwner() {
        return owner_id;
    }

    public void setOwner(int owner) {
        this.owner_id = owner;
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

    public Car(int id, String number, int owner, String model, int year, int price) {

        this.id = id;
        this.number = number;
        this.owner_id = owner;
        this.model = model;
        this.year = year;
        this.price = price;
    }
}
