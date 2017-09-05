package com.example.megakit_10;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Kalevych_tech@ukr.net on 05.09.2017.
 */

public class CarsProvider extends ContentProvider{
    final String LOG_TAG = "myLogs";

    // Создаем константы для БД
    static final String DB_NAME = "dbCarsAndOwners";
    static final int DB_VERSION = 1;

    // Таблицы автомобилей и владельцев
    static final String CARS_TABLE = "cars";
    static final String OWNERS_TABLE = "owners";

    // Поля для таблицы автомобилей
    static final String CARS_ID = "_id";
    static final String CARS_MODEL = "model";
    static final String CARS_OWNER = "owner";
    static final String CARS_NUMBER = "number";
    static final String CARS_YEAR = "year";
    static final String CARS_PRICE = "price";
    static final String CARS_IN_ORDER = "in_order";

    // Поля для таблицы владельцев
    static final String OWNERS_ID = "_id";
    static final String OWNERS_FIRST_NAME = "firstname";
    static final String OWNERS_AGE = "age";
    static final String OWNERS_LAST_NAME = "last_name";
    static final String OWNERS_TELEPHONE = "telephone";
    static final String OWNERS_MAIL = "mail";

    // authority
    static final String AUTHORITY = "com.example.megakit_10.carsbase";

    // path
    static final String CARS_PATH = "cars";
    static final String OWNERS_PATH = "owners";
    // Общий Uri
    public static final Uri CARS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + CARS_PATH);
    public static final Uri OWNERS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + OWNERS_PATH);

    // Скрипт создания таблицы автомобилей
    static final String DB_CARS_CREATE = "create table " + CARS_TABLE + "("
            + CARS_ID + " integer primary key autoincrement, "
            + CARS_NUMBER + " text, " + CARS_OWNER + " integer, "
            + CARS_YEAR + " integer, "
            + CARS_PRICE + " integer, " + CARS_IN_ORDER + " boolean, "
            + CARS_MODEL + " text, foreign key("
            + CARS_OWNER + ") references "+ OWNERS_TABLE + "(" + OWNERS_ID + ")" + ");";

    // Скрипт создания таблицы владельцев
    static final String DB_OWNERS_CREATE = "create table " + OWNERS_TABLE + "("
            + OWNERS_ID + " integer primary key autoincrement, "
            + OWNERS_FIRST_NAME + " text, "
            + OWNERS_LAST_NAME + " text, "
            + OWNERS_AGE + " integer, "
            + OWNERS_TELEPHONE + " text, "
            + OWNERS_MAIL + " text" + ");";

    //скрипты для удаления таблиц, если потребуется при апдейте
    static final String TABLE_CARS_DROP = "DROP TABLE IF EXISTS " + CARS_TABLE ;
    static final String TABLE_OWNERS_DROP = "DROP TABLE IF EXISTS "+ OWNERS_TABLE ;




    // Типы данных
    // набор строк
    static final String CARS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + CARS_PATH;
    static final String OWNERS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + OWNERS_PATH;

    // одна строка
    static final String CARS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + CARS_PATH;
    static final String OWNERS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + OWNERS_PATH;


    // общий Uri
    static final int URI_CARS = 11;
    static final int URI_OWNERS = 21;

    // Uri с указанным ID
    static final int URI_CARS_ID = 12;
    static final int URI_OWNERS_ID = 22;

    // описание и создание UriMatcher
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, CARS_PATH, URI_CARS);
        uriMatcher.addURI(AUTHORITY, CARS_PATH + "/#", URI_CARS_ID);

        uriMatcher.addURI(AUTHORITY, OWNERS_PATH, URI_OWNERS);
        uriMatcher.addURI(AUTHORITY, OWNERS_PATH + "/#", URI_OWNERS_ID);
    }

    DBHelper dbHelper;
    SQLiteDatabase db;

    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");

        dbHelper = new DBHelper(getContext());

        return true;
    }

    // чтение
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Log.d(LOG_TAG, "query, " + uri.toString());
        // проверяем Uri
        switch (uriMatcher.match(uri)) {
            case URI_CARS: // общий Uri
                Log.d(LOG_TAG, "URI_CARS");
                // если сортировка не указана, ставим свою - по id
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = CARS_ID + " ASC";
                }
                break;
            case URI_CARS_ID: // Uri с ID
                String id_car = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CARS_ID, " + id_car);
                // добавляем ID к условию выборки
                if (TextUtils.isEmpty(selection)) {
                    selection = CARS_ID + " = " + id_car;
                } else {
                    selection = selection + " AND " + CARS_ID + " = " + id_car;
                }
                break;
            case URI_OWNERS: // общий Uri
                Log.d(LOG_TAG, "URI_OWNERS");
                // если сортировка не указана, ставим свою - по id
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = OWNERS_ID + " ASC";
                }
                break;
            case URI_OWNERS_ID: // Uri с ID
                String id_owner = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_OWNERS_ID, " + id_owner);
                // добавляем ID к условию выборки
                if (TextUtils.isEmpty(selection)) {
                    selection = OWNERS_ID + " = " + id_owner;
                } else {
                    selection = selection + " AND " + OWNERS_ID + " = " + id_owner;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        //?????? не может создать таблицы, метод не хочет вызываться nullPoinerException
       /* Log.d(LOG_TAG,"try to call onCreate DBHelper");
        dbHelper = new DBHelper(getContext());*/
        //dbHelper.createTables();

        db = dbHelper.getWritableDatabase();
        Cursor cursor;
        if((uriMatcher.match(uri)==URI_CARS)||(uriMatcher.match(uri)==URI_CARS_ID)){
         cursor = db.query(CARS_TABLE, projection, selection,
                selectionArgs, null, null, sortOrder);
        // просим ContentResolver уведомлять этот курсор
        // об изменениях данных в CONTACT_CONTENT_URI
        cursor.setNotificationUri(getContext().getContentResolver(),
                CARS_CONTENT_URI);}
        else{
            cursor = db.query(OWNERS_TABLE, projection, selection,
                selectionArgs, null, null, sortOrder);
            // просим ContentResolver уведомлять этот курсор
            // об изменениях данных в CONTACT_CONTENT_URI
            cursor.setNotificationUri(getContext().getContentResolver(),
                    OWNERS_CONTENT_URI);}
        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {

            /*db = dbHelper.getWritableDatabase();
            db.execSQL(DB_CARS_DROP);
            db.execSQL(DB_DRIVERS_DROP);*/


        Log.d(LOG_TAG, "insert, " + uri.toString());

        if ((uriMatcher.match(uri)!=URI_CARS)&&(uriMatcher.match(uri)!=URI_OWNERS))
            throw new IllegalArgumentException("Wrong URI: " + uri);
        if (uriMatcher.match(uri) == URI_CARS){


        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(CARS_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(CARS_CONTENT_URI, rowID);
        // уведомляем ContentResolver, что данные по адресу resultUri изменились
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;}
        if (uriMatcher.match(uri) == URI_OWNERS){


            db = dbHelper.getWritableDatabase();
            long rowID = db.insert(OWNERS_TABLE, null, values);
            Uri resultUri = ContentUris.withAppendedId(OWNERS_CONTENT_URI, rowID);
            // уведомляем ContentResolver, что данные по адресу resultUri изменились
            getContext().getContentResolver().notifyChange(resultUri, null);
            return resultUri;}

        return uri;
    }

  //функция удаления записи, сначала определяем, откуда нужно удалить, потом обращаемся к базе
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete, " + uri.toString());
        String CURRENT_TABLE = CARS_TABLE;
        switch (uriMatcher.match(uri)) {
            case URI_CARS:
                Log.d(LOG_TAG, "URI_CARS");
                break;
            case URI_CARS_ID:
                String id_cars = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CARS_ID, " + id_cars);
                if (TextUtils.isEmpty(selection)) {
                    selection = CARS_ID + " = " + id_cars;
                } else {
                    selection = selection + " AND " + CARS_ID + " = " + id_cars;
                }
                break;
            case URI_OWNERS:
                Log.d(LOG_TAG, "URI_CARS");
                CURRENT_TABLE = OWNERS_TABLE;
                break;
            case URI_OWNERS_ID:
                String id_owners = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_OWNERS_ID, " + id_owners);
                if (TextUtils.isEmpty(selection)) {
                    selection = OWNERS_ID + " = " + id_owners;
                } else {
                    selection = selection + " AND " + OWNERS_ID + " = " + id_owners;
                }
                CURRENT_TABLE = OWNERS_TABLE;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(CURRENT_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    //функция обновления записи, сначала определяем, где нужно обновить, потом обращаемся к базе
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(LOG_TAG, "update, " + uri.toString());
        String CURRENT_TABLE = CARS_TABLE;
        switch (uriMatcher.match(uri)) {
            case URI_CARS:
                Log.d(LOG_TAG, "URI_CARS");

                break;
            case URI_CARS_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CARS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = CARS_ID + " = " + id;
                } else {
                    selection = selection + " AND " + CARS_ID + " = " + id;
                }
                break;
            case URI_OWNERS:
                Log.d(LOG_TAG, "URI_OWNERS");
                CURRENT_TABLE = OWNERS_TABLE;
                break;
            case URI_OWNERS_ID:
                String id_drivers = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_OWNERS_ID, " + id_drivers);
                if (TextUtils.isEmpty(selection)) {
                    selection = OWNERS_ID + " = " + id_drivers;
                } else {
                    selection = selection + " AND " + OWNERS_ID + " = " + id_drivers;
                }
                CURRENT_TABLE = OWNERS_TABLE;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(CURRENT_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_CARS:
                return CARS_CONTENT_TYPE;
            case URI_CARS_ID:
                return CARS_CONTENT_ITEM_TYPE;
            case URI_OWNERS:
                return OWNERS_CONTENT_TYPE;
            case URI_OWNERS_ID:
                return OWNERS_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    //клас для работы с базой даных, при создании инициализирует таблицы
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
   Log.d(LOG_TAG,"onCreate in DBHelper called");
            db.execSQL(DB_OWNERS_CREATE);
            ContentValues cv2 = new ContentValues();
            for (int i = 1; i <= 3; i++) {
                cv2.put(OWNERS_FIRST_NAME, "firstname " + i);
                cv2.put(OWNERS_LAST_NAME, "last_name " + i);
                cv2.put(OWNERS_AGE, 20);
                cv2.put(OWNERS_TELEPHONE, "telephone " + i);
                cv2.put(OWNERS_MAIL, "mail " + i);

                db.insert(OWNERS_TABLE, null, cv2);
            }

            db.execSQL(DB_CARS_CREATE);

            ContentValues cv = new ContentValues();
            for (int i = 1; i <= 3; i++) {
                cv.put(CARS_NUMBER, "number " + i);
                cv.put(CARS_OWNER, i);
                cv.put(CARS_MODEL, "model "+i );
                cv.put(CARS_YEAR, 2000);
                cv.put(CARS_PRICE, 100000);
                cv.put(CARS_IN_ORDER, true);

                db.insert(CARS_TABLE, null, cv);
            }


        }


// возможно когда-нибудь пригодится обновлять саму базу
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
