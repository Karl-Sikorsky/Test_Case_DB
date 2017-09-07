package com.example.megakit_10;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity implements DialogFragmentEditTableListener, LoaderManager.LoaderCallbacks<Cursor> {

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

    final String OWNERS_ID = "_id";
    final String OWNERS_AGE = "age";
    final String OWNERS_LASTNAME = "lastname";
    final String OWNERS_FIRSTNAME = "firstname";
    final String OWNERS_TELEPHON = "telephone";
    final String OWNERS_MAIL = "mail";

    final int QUERY_CODE = 0;
    final int INSERT_CODE = 1;
    final int UPDATE_CODE = 2;
    final int DELETE_CODE = 3;


    EditText edT;
    String CURRENT_TABLE;
    Uri CURRENT_URI;
    //ListView lvContact,lvOwners;
    DialogFragment dialogEdit, dialogEditOwners;
    CarsRVAdapter adapter;
    OwnersRVAdapter adapterOwners;
    Cursor cursor_RV;
            Cursor cursor_RV_Owners;

    TabHost.TabSpec tabSpec;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //edT = (EditText)findViewById(R.id.editText) ;


        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        // инициализация
        tabHost.setup();

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Владельцы");
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Авто");
        tabSpec.setContent(R.id.tab2);
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTabByTag("tag2");
        CURRENT_URI = CARS_URI;
        dialogEdit = new DialogFragmentEditTableCars();
        dialogEditOwners = new DialogFragmentEditTableOwners();

        List<Car> cars = new ArrayList<>();
        List<Owner> owners = new ArrayList<>();
        cursor_RV_Owners = getContentResolver().query(OWNERS_URI, null, null,
                null, null);
        cursor_RV = getContentResolver().query(CARS_URI, null, null,
                null, null);

        getLoaderManager().initLoader(0, null, this);



        RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerView);
        RecyclerView rvOwners = (RecyclerView)findViewById(R.id.recyclerViewOwners) ;
        //Log.d(LOG_TAG, String.valueOf(cars.size()));
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        LinearLayoutManager llmOwners = new LinearLayoutManager(getApplicationContext());
        rvOwners.setLayoutManager(llmOwners);
        adapter = new CarsRVAdapter(getApplicationContext(), cursor_RV);
        adapterOwners = new OwnersRVAdapter(getApplicationContext(),cursor_RV_Owners);
        rv.setAdapter(adapter);
        rvOwners.setAdapter(adapterOwners);



        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                CURRENT_URI = CURRENT_URI==OWNERS_URI ? CARS_URI : OWNERS_URI;
                Log.d(LOG_TAG,"current uri is "+ CURRENT_URI);
            }
        });

       // Cursor cursor_cars = getContentResolver().query(CARS_URI, null, null,null, null);
        //startManagingCursor(cursor_cars);

       // lvContact = (ListView) findViewById(R.id.lvContact);

        /*String from[] = { "number", "_id" };
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


        lvOwners.setAdapter(adapter_drivers);*/
    }
public void redrawList(){

}
   /* public void onClickInsert(View v) {
        Uri newUri = null;
        if(CURRENT_URI==CARS_URI){
        ContentValues cv = new ContentValues();
        cv.put(CARS_NUMBER, "inserted number");
        cv.put(CARS_YEAR, 1972);
            cv.put(CARS_OWNER, "inserted owner");
        newUri = getContentResolver().insert(CURRENT_URI, cv);}
        if(CURRENT_URI==OWNERS_URI){
            ContentValues cv = new ContentValues();
            cv.put(OWNERS_LASTNAME, "gosling");
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

    }*/

    public void onClickEditCars(View view) {
        dialogEdit.show(getFragmentManager(),"edit dialog tag");

    }
    public void onClickEditOwners(View view){
        dialogEditOwners.show(getFragmentManager(),"edit dialog owners tag");
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
                   adapter.swapCursor(cursor_RV);}else{
                   cursor_RV_Owners = getContentResolver().query(OWNERS_URI, null, SELECTION, null, null);
                   adapterOwners.swapCursor(cursor_RV_Owners);
               }


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



                   newUri = getContentResolver().insert(CURRENT_URI, cv);

                   cursor_RV = getContentResolver().query(CARS_URI, null, null, null, null);
                   adapter.swapCursor(cursor_RV);
               }
               if(CURRENT_URI==OWNERS_URI){
                   ContentValues cv = new ContentValues();

                   if(params.containsKey(OWNERS_FIRSTNAME))cv.put(OWNERS_FIRSTNAME, params.get(OWNERS_FIRSTNAME));
                   if(params.containsKey(OWNERS_LASTNAME))cv.put(OWNERS_LASTNAME, params.get(OWNERS_LASTNAME));
                   if(params.containsKey(OWNERS_AGE))cv.put(OWNERS_AGE, Integer.parseInt(params.get(OWNERS_AGE)));
                   if(params.containsKey(OWNERS_TELEPHON))cv.put(OWNERS_TELEPHON, params.get(OWNERS_TELEPHON));
                   if(params.containsKey(OWNERS_MAIL))cv.put(OWNERS_MAIL, params.get(OWNERS_MAIL));



                   newUri = getContentResolver().insert(CURRENT_URI, cv);

                   cursor_RV_Owners = getContentResolver().query(OWNERS_URI, null, null, null, null);
                   adapterOwners.swapCursor(cursor_RV_Owners);
               }

               Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());



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
               else{
                   int cnt = getContentResolver().delete(OWNERS_URI, SELECTION, null);
                   cursor_RV_Owners = getContentResolver().query(OWNERS_URI, null, null, null, null);
                   Log.d(LOG_TAG,"count of delete: " + cnt);
                   adapterOwners.swapCursor(cursor_RV_Owners);
               }
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
        else{
            ContentValues cv = new ContentValues();
            if(paramsTo.containsKey(OWNERS_FIRSTNAME))cv.put(OWNERS_FIRSTNAME, paramsTo.get(OWNERS_FIRSTNAME));
            if(paramsTo.containsKey(OWNERS_LASTNAME))cv.put(OWNERS_LASTNAME, paramsTo.get(OWNERS_LASTNAME));
            if(paramsTo.containsKey(OWNERS_AGE))cv.put(OWNERS_AGE, Integer.parseInt(paramsTo.get(OWNERS_AGE)));
            if(paramsTo.containsKey(OWNERS_TELEPHON))cv.put(OWNERS_TELEPHON, paramsTo.get(OWNERS_TELEPHON));
            if(paramsTo.containsKey(OWNERS_MAIL))cv.put(OWNERS_MAIL, paramsTo.get(OWNERS_MAIL));

            String SELECTION = "";

            for(HashMap.Entry entry : paramsFrom.entrySet()){
                SELECTION = SELECTION + (((String) entry.getKey())+((String) entry.getValue()))+ " AND ";
            }
            if(!SELECTION.equals("")){
                SELECTION = SELECTION.substring(0,SELECTION.length()-5);
                Log.d(LOG_TAG, SELECTION);}


            int cnt = getContentResolver().update(OWNERS_URI,cv, SELECTION, null);

            Log.d(LOG_TAG,"count of update: " + cnt);
            cursor_RV_Owners = getContentResolver().query(OWNERS_URI, null, null, null, null);
            adapterOwners.swapCursor(cursor_RV_Owners);
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,CURRENT_URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
     adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    adapter.swapCursor(null);
    }
}
