package com.example.megakit_10;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;

public class MainActivity extends Activity {

    final String LOG_TAG = "myLogs";

    final Uri CARS_URI = Uri
            .parse("content://com.example.megakit_10.carsbase/cars");
    final Uri OWNERS_URI = Uri
            .parse("content://com.example.megakit_10.carsbase/owners");

    final String CARS_NUMBER = "number";
    final String CARS_YEAR = "year";
    final String CARS_OWNER = "owner";

    final String OWNERS_AGE = "age";
    final String OWNERS_LAST_NAME = "last_name";


    EditText edT;
    String CURRENT_TABLE;
    Uri CURRENT_URI;
    ListView lvContact,lvOwners;

    TabHost.TabSpec tabSpec;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edT = (EditText)findViewById(R.id.editText) ;


        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        // инициализация
        tabHost.setup();

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Авто");
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Владельцы");
        tabSpec.setContent(R.id.tab2);
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTabByTag("tag2");

        CURRENT_URI = CARS_URI;
        Cursor cursor_cars = getContentResolver().query(CARS_URI, null, null,
                null, null);
        startManagingCursor(cursor_cars);

        lvContact = (ListView) findViewById(R.id.lvContact);

        String from[] = { "number", "_id" };
        int to[] = { android.R.id.text1, android.R.id.text2 };
        SimpleCursorAdapter adapter_cars = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, cursor_cars, from, to);


        lvContact.setAdapter(adapter_cars);

        Cursor cursor_drivers = getContentResolver().query(OWNERS_URI, null, null,
                null, null);
        startManagingCursor(cursor_drivers);

        lvOwners = (ListView) findViewById(R.id.lvDrivers);

        String fromd[] = { "telephone", "_id" };
        int tod[] = { android.R.id.text1, android.R.id.text2 };
        SimpleCursorAdapter adapter_drivers = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, cursor_drivers, fromd, tod);


        lvOwners.setAdapter(adapter_drivers);
    }
public void redrawList(){

}
    public void onClickInsert(View v) {
        Uri newUri = null;
        if(CURRENT_URI==CARS_URI){
        ContentValues cv = new ContentValues();
        cv.put(CARS_NUMBER, "inserted number");
        cv.put(CARS_YEAR, 1972);
            cv.put(CARS_OWNER, "inserted owner");
        newUri = getContentResolver().insert(CURRENT_URI, cv);}
        if(CURRENT_URI==OWNERS_URI){
            ContentValues cv = new ContentValues();
            cv.put(OWNERS_LAST_NAME, "gosling");
            cv.put(OWNERS_AGE, 29);
            newUri = getContentResolver().insert(CURRENT_URI, cv);}
        Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());
    }

    public void onClickUpdate(View v) {
        if(CURRENT_URI==CARS_URI) {
            ContentValues cv = new ContentValues();
            cv.put(CARS_NUMBER, "lamborgini");
            cv.put(CARS_YEAR, 1973);
            cv.put(CARS_OWNER, "gosling");
            Uri uri = ContentUris.withAppendedId(CURRENT_URI, Integer.parseInt(String.valueOf(edT.getText())));
            int cnt = getContentResolver().update(uri, cv, null, null);
            Log.d(LOG_TAG, "update, count = " + cnt);
        }
        if(CURRENT_URI==OWNERS_URI) {
            ContentValues cv = new ContentValues();
            cv.put(OWNERS_AGE, 35);
            cv.put(OWNERS_LAST_NAME, "gosling");
            Uri uri = ContentUris.withAppendedId(CURRENT_URI, Integer.parseInt(String.valueOf(edT.getText())));
            int cnt = getContentResolver().update(uri, cv, null, null);
            Log.d(LOG_TAG, "update, count = " + cnt);
        }
    }

    public void onClickDelete(View v) {
        Uri uri = ContentUris.withAppendedId(CURRENT_URI, Integer.parseInt(String.valueOf(edT.getText())));
        int cnt = getContentResolver().delete(uri, null, null);
        Log.d(LOG_TAG, "delete, count = " + cnt);
    }

    public void onClickError(View v) {

        if(CURRENT_URI==CARS_URI){CURRENT_URI =OWNERS_URI;}else{CURRENT_URI=CARS_URI;};

    }
}
