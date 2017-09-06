package com.example.megakit_10;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements DialogFragmentEditTableListener {

    final String LOG_TAG = "myLogs";

    final Uri CARS_URI = Uri
            .parse("content://com.example.megakit_10.carsbase/cars");
    final Uri OWNERS_URI = Uri
            .parse("content://com.example.megakit_10.carsbase/owners");

    final String CARS_NUMBER = "number";
    final String CARS_YEAR = "year";
    final String CARS_OWNER = "owner";
    final String CARS_ID = "_id";
    final String CARS_PRICE = "price";
    final String CARS_MODEL = "model";

    final String OWNERS_AGE = "age";
    final String OWNERS_LAST_NAME = "last_name";
    final int QUERY_CODE = 0;
    final int INSERT_CODE = 1;
    final int UPDATE_CODE = 2;
    final int DELETE_CODE = 3;


    EditText edT;
    String CURRENT_TABLE;
    Uri CURRENT_URI;
    ListView lvContact,lvOwners;
    DialogFragment dialogEdit;
    CarsRVAdapter adapter;
    Cursor cursor_RV;

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

        dialogEdit = new DialogFragmentEditTable();


        List<Auto> cars = new ArrayList<>();
        cursor_RV = getContentResolver().query(CARS_URI, null, null,
                null, null);


        RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerView);
        Log.d(LOG_TAG, String.valueOf(cars.size()));
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        adapter = new CarsRVAdapter(getApplicationContext(), cursor_RV);
        rv.setAdapter(adapter);

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

    public void onClickEdit(View view) {
        dialogEdit.show(getFragmentManager(),"edit dialog tag");

    }

    @Override
    public void onDialogDoneClick(DialogFragment dialogFragment, int operation, HashMap<String, String> params) {
     Log.d(LOG_TAG,"onDialogDoneClick called");
        String SELECTION;
       switch (operation){
           case QUERY_CODE:
                SELECTION = "";
               int i=0;
               for(HashMap.Entry entry : params.entrySet()){
                SELECTION = SELECTION + (((String) entry.getKey())+((String) entry.getValue()))+ " AND ";
               }
               if(!SELECTION.equals("")){
               SELECTION = SELECTION.substring(0,SELECTION.length()-5);
               Log.d(LOG_TAG, SELECTION);}

               if(CURRENT_URI==CARS_URI){
               cursor_RV = getContentResolver().query(CARS_URI, null, SELECTION, null, null);
                   adapter.swapCursor(cursor_RV);}


           break;
           case INSERT_CODE:
               Uri newUri = null;
               if(CURRENT_URI==CARS_URI){
                   ContentValues cv = new ContentValues();

                   if(params.containsKey(CARS_NUMBER))cv.put(CARS_NUMBER, params.get(CARS_NUMBER));
                   if(params.containsKey(CARS_OWNER))cv.put(CARS_OWNER, Integer.parseInt(params.get(CARS_OWNER)));
                   if(params.containsKey(CARS_MODEL))cv.put(CARS_MODEL, params.get(CARS_MODEL));
                   if(params.containsKey(CARS_YEAR))cv.put(CARS_YEAR, Integer.parseInt(params.get(CARS_YEAR)));
                   if(params.containsKey(CARS_PRICE))cv.put(CARS_PRICE, Integer.parseInt(params.get(CARS_PRICE)));



                   newUri = getContentResolver().insert(CURRENT_URI, cv);}
               if(CURRENT_URI==OWNERS_URI){
                   ContentValues cv = new ContentValues();
                   cv.put(OWNERS_LAST_NAME, "gosling");
                   cv.put(OWNERS_AGE, 29);
                   newUri = getContentResolver().insert(CURRENT_URI, cv);}
               Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());

               cursor_RV = getContentResolver().query(CARS_URI, null, null, null, null);
               adapter.swapCursor(cursor_RV);

               //adapter.notifyDataSetChanged();
               Log.d(LOG_TAG,cursor_RV.toString());
           break;
           case UPDATE_CODE:

           break;
           case DELETE_CODE:
               SELECTION = "";

               for(HashMap.Entry entry : params.entrySet()){
                   SELECTION = SELECTION + (((String) entry.getKey())+((String) entry.getValue()))+ " AND ";
               }
               if(!SELECTION.equals("")){
                   SELECTION = SELECTION.substring(0,SELECTION.length()-5);
                   Log.d(LOG_TAG, SELECTION);}

               if(CURRENT_URI==CARS_URI){
                   int cnt = getContentResolver().delete(CARS_URI, SELECTION, null);
                   cursor_RV = getContentResolver().query(CARS_URI, null, null, null, null);
                   Log.d(LOG_TAG,"count of delete: " + cnt);
                   adapter.swapCursor(cursor_RV);}
           break;
       }
    }

    @Override
    public void onDialogDoneClickUpdate(DialogFragment dialogFragment, int operation, HashMap<String, String> paramsFrom, HashMap<String, String> paramsTo) {
        Log.d(LOG_TAG, "data in update method ");
        for(HashMap.Entry entry : paramsFrom.entrySet()){
            Log.d(LOG_TAG,((String) entry.getKey())+((String) entry.getValue()));
        }
        Log.d(LOG_TAG, "data in update method target ");
        for(HashMap.Entry entry : paramsTo.entrySet()){
            Log.d(LOG_TAG,((String) entry.getKey())+((String) entry.getValue()));
        }


        if(CURRENT_URI==CARS_URI){
            ContentValues cv = new ContentValues();
            if(paramsTo.containsKey(CARS_NUMBER))cv.put(CARS_NUMBER, paramsTo.get(CARS_NUMBER));

            if(paramsTo.containsKey(CARS_OWNER))cv.put(CARS_OWNER, Integer.parseInt(paramsTo.get(CARS_OWNER)));
            if(paramsTo.containsKey(CARS_MODEL))cv.put(CARS_MODEL, paramsTo.get(CARS_MODEL));
            if(paramsTo.containsKey(CARS_YEAR))cv.put(CARS_YEAR, Integer.parseInt(paramsTo.get(CARS_YEAR)));
            if(paramsTo.containsKey(CARS_PRICE))cv.put(CARS_PRICE, Integer.parseInt(paramsTo.get(CARS_PRICE)));

            String SELECTION = "";

            for(HashMap.Entry entry : paramsFrom.entrySet()){
                SELECTION = SELECTION + (((String) entry.getKey())+((String) entry.getValue()))+ " AND ";
            }
            if(!SELECTION.equals("")){
                SELECTION = SELECTION.substring(0,SELECTION.length()-5);
                Log.d(LOG_TAG, SELECTION);}


                int cnt = getContentResolver().update(CARS_URI,cv, SELECTION, null);

                Log.d(LOG_TAG,"count of update: " + cnt);
            cursor_RV = getContentResolver().query(CARS_URI, null, null, null, null);
            adapter.swapCursor(cursor_RV);
        }

    }
}
