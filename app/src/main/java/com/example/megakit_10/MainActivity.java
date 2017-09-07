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
//Класс получился очень большим, в скорейшем времени разнесу его согласно какой-то архитектуре
// в требованиях к ТЗ было желательно реализовать работу с локальной базой через REST API
// такой подход позволяет реализовать ContentProvider
public class MainActivity extends Activity implements DialogFragmentEditTableListener, LoaderManager.LoaderCallbacks<Cursor> {
    //константы
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




    Uri CURRENT_URI;

    DialogFragment dialogEdit, dialogEditOwners;
    CarsRVAdapter adapter;
    OwnersRVAdapter adapterOwners;
    Cursor cursor_RV;
            Cursor cursor_RV_Owners;

    TabHost.TabSpec tabSpec;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         //реализация вкладок для удобной навигации между таблицами
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
        //текущее Юри будет изменяться в слушателе ищменения вкладки
        CURRENT_URI = CARS_URI;

        //инициализация Диалогов редактирования Базы
        dialogEdit = new DialogFragmentEditTableCars();
        dialogEditOwners = new DialogFragmentEditTableOwners();

       //курсоры для таблиц
        cursor_RV_Owners = getContentResolver().query(OWNERS_URI, null, null,
                null, null);
        cursor_RV = getContentResolver().query(CARS_URI, null, null,
                null, null);

        getLoaderManager().initLoader(0, null, this);



        RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerView);
        RecyclerView rvOwners = (RecyclerView)findViewById(R.id.recyclerViewOwners) ;

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        LinearLayoutManager llmOwners = new LinearLayoutManager(getApplicationContext());
        rvOwners.setLayoutManager(llmOwners);
        adapter = new CarsRVAdapter(getApplicationContext(), cursor_RV);
        adapterOwners = new OwnersRVAdapter(getApplicationContext(),cursor_RV_Owners);
        rv.setAdapter(adapter);
        rvOwners.setAdapter(adapterOwners);


       //смена текущего Юри в зависимости от выбраной вкладки
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                CURRENT_URI = CURRENT_URI==OWNERS_URI ? CARS_URI : OWNERS_URI;
                Log.d(LOG_TAG,"current uri is "+ CURRENT_URI);
            }
        });


    }
         // методы для вызова диалоговых окон редактирования таблиц
    public void onClickEditCars(View view) {
        dialogEdit.show(getFragmentManager(),"edit dialog tag");

    }
    public void onClickEditOwners(View view){
        dialogEditOwners.show(getFragmentManager(),"edit dialog owners tag");
    }
    //методы, вызываемые диалогами после подтверждения редактирования базы пользователем
    @Override
    public void onDialogDoneClick(DialogFragment dialogFragment, int operation, HashMap<String, String> params) {
     Log.d(LOG_TAG,"onDialogDoneClick called");
        String SELECTION;
       switch (operation){
           //отображения выбраных данных
           case QUERY_CODE:
                SELECTION = "";
               int i=0;
               for(HashMap.Entry entry : params.entrySet()){
                SELECTION = SELECTION + (((String) entry.getKey())+((String) entry.getValue()))+ " AND ";
               }
               //вот тут костыль. Мы по факту формируем строковый запрос к базе на основании данных
               //введенных пользователем, и следующие строки нужны для приведения запроса в желаемый вид
               if(!SELECTION.equals("")){
               SELECTION = SELECTION.substring(0,SELECTION.length()-5);
               Log.d(LOG_TAG, SELECTION);}

               //сам запрос и обновление интерфейса согласно его результатам в зависимости от выбраной таблицы
               if(CURRENT_URI==CARS_URI){
               cursor_RV = getContentResolver().query(CARS_URI, null, SELECTION, null, null);
                   adapter.swapCursor(cursor_RV);}else{
                   cursor_RV_Owners = getContentResolver().query(OWNERS_URI, null, SELECTION, null, null);
                   adapterOwners.swapCursor(cursor_RV_Owners);
               }

           break;
           //если пользователем было выбрано добавление записей
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

           break;
           case UPDATE_CODE:

           break;
           //если пользователем было выбрано удаление
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

    //отдельная функция для обновления записей, так как она требует другого вида взаимодействия с диалоговым окном
    @Override
    public void onDialogDoneClickUpdate(DialogFragment dialogFragment, int operation, HashMap<String, String> paramsFrom, HashMap<String, String> paramsTo) {

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
   //переопределение методов Лоадера
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
